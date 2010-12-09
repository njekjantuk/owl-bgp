package org.semanticweb.sparql.bgpevaluation;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.semanticweb.HermiT.HermiTCostEstimationVisitor;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.sparql.OWLReasonerSPARQLEngine;
import org.semanticweb.sparql.arq.OWLBGPQueryIterator;
import org.semanticweb.sparql.arq.OWLOntologyGraph;
import org.semanticweb.sparql.bgpevaluation.queryobjects.AxiomTemplateToQueryObjectConverter;
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
            AxiomVisitorEx<QueryObject<? extends Axiom>> axiomVisitor=new AxiomTemplateToQueryObjectConverter(dataFactory,positionInTuple,graph);
            List<QueryObject<? extends Axiom>> queryObjects=new ArrayList<QueryObject<? extends Axiom>>();
            for (Axiom axiom : queryAxiomTemplates) {
                QueryObject<? extends Axiom> at=axiom.accept(axiomVisitor);
                if (at!=null) // should be all not null eventually
                    queryObjects.add(at);
            }
            Atomic[] initialBinding=new Atomic[positionInTuple.keySet().size()];
            List<Atomic[]> bindings=new ArrayList<Atomic[]>();
            bindings.add(initialBinding);
            CostEstimationVisitor costEstimator;
            if (reasoner instanceof Reasoner)
                costEstimator=new HermiTCostEstimationVisitor(graph,positionInTuple,bindings);
            else 
                costEstimator=new CostEstimationVisitor(graph,positionInTuple,bindings);
//            System.out.println("Start evaluation: ");
            long t;
            long t_cost=0;
            long t_compute=0;
            while (!queryObjects.isEmpty() && !bindings.isEmpty()) {
//                System.out.println("Costs: ");
//                for (QueryObject<? extends Axiom> qo : queryObjects) {
//                    t=System.currentTimeMillis();
//                    double[] cost=qo.accept(costEstimator);
//                    t_cost+=(System.currentTimeMillis()-t);
//                    System.out.println(qo+", cost: "+cost[0]+", results: "+cost[1]);
//                }
                QueryObject<? extends Axiom> cheapest=QueryReordering.getCheapest(costEstimator, queryObjects);
                queryObjects.remove(cheapest);
//                System.out.println("cheaptest: "+cheapest);
                t=System.currentTimeMillis();
                bindings=cheapest.computeBindings(reasoner, graph, bindings, positionInTuple);
                t_compute+=(System.currentTimeMillis()-t);
//                System.out.println("Cost estimation time: "+t_cost+", computation time: "+t_compute+", results size: "+bindings.size());
                costEstimator.updateCandidateBindings(bindings);
            }
            System.out.println("Cost estimation time: "+t_cost+", computation time: "+t_compute+", results size: "+bindings.size());
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
