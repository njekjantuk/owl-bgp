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
    protected final Map<Variable,Integer> positionInTuple;
    protected final Set<String> m_skolemConstants;
    protected QueryIterator input;
    protected List<Atomic[]> results;
    protected int currentRow;

    public OWLBGPQueryIterator(QueryIterator input,ExecutionContext execCxt,List<Atomic[]> results,Map<Variable,Integer> positionInTuple) {
        m_skolemConstants=((OWLOntologyGraph)execCxt.getActiveGraph()).getSkolemConstants();
        this.positionInTuple=positionInTuple;
        this.input=input;
        this.results=results;
        this.currentRow=0;
    }
    protected boolean hasNextBinding() {
        return currentRow<results.size();
    }
    protected Binding moveToNextBinding() {
        if (hasNextBinding()) {
            Binding bindingMap=new BindingMap();
//            Binding bindingMap=new BindingMap(input);
            Atomic[] result=results.get(currentRow);
            currentRow++;
            for (Variable variable : positionInTuple.keySet()) {
                Var var=Var.alloc(variable.toString());
                if (positionInTuple==null || variable==null || positionInTuple.get(variable)==null || result[positionInTuple.get(variable)]==null) {
                    System.out.println("Ups, we should not be here:");
                    System.out.println("Variable: "+var);
                    System.out.println("position in tuple: "+positionInTuple.get(variable));
                    System.out.println("In this result: ");
                    for (Atomic at : result)
                        System.out.println(at);
                }
                Node node=createNode(result[positionInTuple.get(variable)]);
                bindingMap.add(var,node);
            }
            return bindingMap;
        } else 
            return null;
    }
    protected void closeIterator() {
        input.close();
        input=null;
        results=null;
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