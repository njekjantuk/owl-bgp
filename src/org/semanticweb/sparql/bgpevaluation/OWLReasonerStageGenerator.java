package org.semanticweb.sparql.bgpevaluation;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.semanticweb.HermiT.HermiTCostEstimator;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.sparql.OWLReasonerSPARQLEngine;
import org.semanticweb.sparql.arq.OWLBGPQueryIterator;
import org.semanticweb.sparql.arq.OWLOntologyGraph;
import org.semanticweb.sparql.bgpevaluation.QueryReordering.CostComparator;
import org.semanticweb.sparql.bgpevaluation.queryobjects.AxiomTemplaeToQueryObjectConverter;
import org.semanticweb.sparql.bgpevaluation.queryobjects.QueryObject;
import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.AxiomVisitorEx;
import org.semanticweb.sparql.owlbgp.model.Ontology;
import org.semanticweb.sparql.owlbgp.model.Variable;
import org.semanticweb.sparql.owlbgp.model.axioms.Axiom;
import org.semanticweb.sparql.owlbgp.parser.OWLBGPParser;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.sparql.core.BasicPattern;
import com.hp.hpl.jena.sparql.engine.ExecutionContext;
import com.hp.hpl.jena.sparql.engine.QueryIterator;
import com.hp.hpl.jena.sparql.engine.main.StageGenerator;

public class OWLReasonerStageGenerator implements StageGenerator {
    StageGenerator above=null;
    
    public OWLReasonerStageGenerator(StageGenerator original){
        above=original;
    }
    @Override
    public QueryIterator execute(BasicPattern pattern, QueryIterator input, ExecutionContext execCxt) {
        Graph g = execCxt.getActiveGraph();
        // Test to see if this is a graph we support.  
        if (!(g instanceof OWLOntologyGraph))
            // Not us - bounce up the StageGenerator chain
            return above.execute(pattern, input, execCxt) ;
        OWLOntologyGraph graph=(OWLOntologyGraph)g;
        // Create a QueryIterator for this request
        StringBuffer buffer=new StringBuffer();
        Iterator<Triple> it=pattern.iterator();
        while (it.hasNext()) {
            Triple t=it.next();
            buffer.append(printNode(t.getSubject()));
            buffer.append(" ");
            buffer.append(printNode(t.getPredicate()));
            buffer.append(" ");
            buffer.append(printNode(t.getObject()));
            buffer.append(" . ");
            buffer.append(OWLReasonerSPARQLEngine.LB);
        }
        OWLBGPParser parser=new OWLBGPParser(new StringReader(buffer.toString()));
        parser.loadDeclarations(graph.getClassesInSignature(),graph.getDatatypesInSignature(), graph.getObjectPropertiesInSignature(), graph.getDataPropertiesInSignature(), graph.getAnnotationPropertiesInSignature(), graph.getIndividualsInSignature());
        OWLReasoner reasoner=graph.getReasoner();
        OWLDataFactory dataFactory=reasoner.getRootOntology().getOWLOntologyManager().getOWLDataFactory();
        try {
            parser.parse();
            Ontology queryOntology=parser.getParsedOntology();
            Set<Axiom> queryAxiomTemplates=parser.getParsedAxioms();
            Map<Variable,Integer> positionInTuple=new HashMap<Variable,Integer>();
            int position=0;
            for (Variable var : queryOntology.getVariablesInSignature()) {
                positionInTuple.put(var, position);
                position++;
            } 
            AxiomRewriter.rewriteAxioms(queryAxiomTemplates);
            AxiomVisitorEx<QueryObject<? extends Axiom>> axiomVisitor=new AxiomTemplaeToQueryObjectConverter(dataFactory,positionInTuple,graph);
            Set<QueryObject<? extends Axiom>> queryObjects=new HashSet<QueryObject<? extends Axiom>>();
            for (Axiom axiom : queryAxiomTemplates) {
                QueryObject<? extends Axiom> at=axiom.accept(axiomVisitor);
                if (at!=null) // should be all not null eventually
                    queryObjects.add(at);
            }
            Atomic[] initialBinding=new Atomic[positionInTuple.keySet().size()];
            List<Atomic[]> bindings=new ArrayList<Atomic[]>();
            bindings.add(initialBinding);
            StandardCostEstimator costEstimator;
            if (reasoner instanceof Reasoner)
                costEstimator=new HermiTCostEstimator(graph);
            else 
                costEstimator=new StandardCostEstimator(graph);
            Comparator<QueryObject<? extends Axiom>> costComparator=new CostComparator(costEstimator, bindings, positionInTuple);
            SortedSet<QueryObject<? extends Axiom>> orderedQueryObjects=new TreeSet<QueryObject<? extends Axiom>>(costComparator);
            orderedQueryObjects.addAll(queryObjects);
            for (QueryObject<? extends Axiom> qo : orderedQueryObjects)
                System.out.println(qo);
            for (QueryObject<? extends Axiom> qo : orderedQueryObjects)
                bindings=qo.computeBindings(reasoner, graph, bindings, positionInTuple);
            return new OWLBGPQueryIterator(input,execCxt,bindings,positionInTuple);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return new OWLBGPQueryIterator(input,execCxt,new ArrayList<Atomic[]>(),new HashMap<Variable,Integer>());
        }
    }
    protected String printNode(Node node) {
        if (node.isURI())
            return "<"+node+">";
        else 
            return node.toString();
    }
}
