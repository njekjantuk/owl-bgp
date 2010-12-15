package org.semanticweb.sparql.arq;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.Prefixes;
import org.semanticweb.sparql.owlbgp.model.Variable;
import org.semanticweb.sparql.owlbgp.model.individuals.NamedIndividual;
import org.semanticweb.sparql.owlbgp.model.literals.Literal;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.rdf.model.AnonId;
import com.hp.hpl.jena.sparql.core.Var;
import com.hp.hpl.jena.sparql.engine.ExecutionContext;
import com.hp.hpl.jena.sparql.engine.QueryIterator;
import com.hp.hpl.jena.sparql.engine.binding.Binding;
import com.hp.hpl.jena.sparql.engine.binding.BindingMap;
import com.hp.hpl.jena.sparql.engine.iterator.QueryIteratorBase;
import com.hp.hpl.jena.sparql.serializer.SerializationContext;
import com.hp.hpl.jena.sparql.util.IndentedWriter;

public class OWLBGPQueryIterator extends QueryIteratorBase {
    protected final List<Map<Variable,Integer>> bindingPositionsPerComponent;
    protected final Set<String> m_skolemConstants;
    protected QueryIterator input;
    protected List<List<Atomic[]>> resultsPerComponent;
    protected int currentRow;
    protected int[] m_currentBindingIndexes;
    protected final int numRows;
    
    public OWLBGPQueryIterator(QueryIterator input, ExecutionContext execCxt, List<List<Atomic[]>> results, List<Map<Variable,Integer>> bindingPositionsPerComponent) {
        m_skolemConstants=((OWLOntologyGraph)execCxt.getActiveGraph()).getSkolemConstants();
        this.bindingPositionsPerComponent=bindingPositionsPerComponent;
        this.input=input;
        this.resultsPerComponent=results;
        this.currentRow=0;
        int size=0;
        boolean first=true;
        for (List<Atomic[]> result : resultsPerComponent) {
            if (first) {
                first=false;
                size+=result.size();
            } else {
                size*=result.size();
            }
        }
        numRows=size;
        m_currentBindingIndexes=new int[resultsPerComponent.size()];
    }
    protected boolean hasNextBinding() {
        return currentRow<numRows;
    }
    protected Binding moveToNextBinding() {
        if (hasNextBinding()) {
            Binding bindingMap=new BindingMap();
//            Binding bindingMap=new BindingMap(input);
            boolean flip=false;
            for (int index=resultsPerComponent.size()-1;index>=0;index--) {
                if (index==resultsPerComponent.size()-1) {
                    // last bit, always flip
                    if (m_currentBindingIndexes[index]<resultsPerComponent.get(index).size()-1)
                        m_currentBindingIndexes[index]+=1;
                    else {
                        m_currentBindingIndexes[index]=0;
                        flip=true;
                    }
                } else if (flip) {
                    if (m_currentBindingIndexes[index]<resultsPerComponent.get(index).size()-1) {
                        m_currentBindingIndexes[index]=m_currentBindingIndexes[index]+1;
                        flip=false;
                    } else 
                        m_currentBindingIndexes[index]=0; 
                }
            }
            for (int i=0;i<resultsPerComponent.size();i++) {
                Map<Variable,Integer> positionInTuple=bindingPositionsPerComponent.get(i);
                for (Variable variable : positionInTuple.keySet()) {
                    Var var=Var.alloc(variable.toString());
                    Atomic[] result=resultsPerComponent.get(i).get(m_currentBindingIndexes[i]);
                    Node node=createNode(result[positionInTuple.get(variable)]);
                    bindingMap.add(var,node);
                }
            }
            currentRow++;
            return bindingMap;
        } else 
            return null;
    }
    protected void closeIterator() {
        input.close();
        input=null;
        resultsPerComponent=null;
    }
    public void output(IndentedWriter out, SerializationContext sCxt) {
        // TODO Auto-generated method stub
    }
    protected Node createNode(Atomic atomic) {
        if (atomic instanceof Literal) {
            Literal lit=(Literal)atomic;
            String iri=lit.getDatatype().getIRI().toString(Prefixes.NO_PREFIXES);
            return Node.createLiteral(lit.getLexicalForm(), lit.getLangTag(), Node.getType(iri.substring(1, iri.length()-2)));
        } else {
            // we could do something with the Skolem constants here
            String iri=atomic.toString(Prefixes.NO_PREFIXES);
            iri=iri.substring(1, iri.length()-1);
            if (atomic instanceof NamedIndividual && m_skolemConstants.contains(iri)) {
                String label=iri.substring(2);
                AnonId id=AnonId.create(label);
                Node node=Node.createAnon(id);
                return node;
            } else 
                return Node.createURI(iri);
        } 
    }
}