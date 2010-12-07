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

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.sparql.arq.HermiTGraph;
import org.semanticweb.sparql.arq.HermiTQueryIterator;
import org.semanticweb.sparql.bgpevaluation.QueryReordering.CostComparator;
import org.semanticweb.sparql.evaluation.queryobjects.AxiomTemplaeToQueryObjectConverter;
import org.semanticweb.sparql.evaluation.queryobjects.QueryObject;
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

public class HermiTStageGenerator implements StageGenerator {
    StageGenerator above=null;
    
    public HermiTStageGenerator(StageGenerator original){
        above=original;
    }
    @Override
    public QueryIterator execute(BasicPattern pattern, QueryIterator input, ExecutionContext execCxt) {
        Graph g = execCxt.getActiveGraph();
        // Test to see if this is a graph we support.  
        if (!(g instanceof HermiTGraph))
            // Not us - bounce up the StageGenerator chain
            return above.execute(pattern, input, execCxt) ;
        HermiTGraph graph=(HermiTGraph)g;
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
            buffer.append(HermiTSPARQLEngine.LB);
        }
        OWLBGPParser parser=new OWLBGPParser(new StringReader(buffer.toString()));
        parser.loadDeclarations(graph.getClassesInSignature(),graph.getDatatypesInSignature(), graph.getObjectPropertiesInSignature(), graph.getDataPropertiesInSignature(), graph.getAnnotationPropertiesInSignature(), graph.getIndividualsInSignature());
        
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
            Reasoner hermit=graph.getReasoner();
            AxiomVisitorEx<QueryObject<? extends Axiom>> axiomVisitor=new AxiomTemplaeToQueryObjectConverter(hermit.getDataFactory(),positionInTuple,graph);
            Set<QueryObject<? extends Axiom>> queryObjects=new HashSet<QueryObject<? extends Axiom>>();
            for (Axiom axiom : queryAxiomTemplates) {
                QueryObject<? extends Axiom> at=axiom.accept(axiomVisitor);
                if (at!=null) // should be all not null eventually
                    queryObjects.add(at);
            }
            Atomic[] initialBinding=new Atomic[positionInTuple.keySet().size()];
            List<Atomic[]> bindings=new ArrayList<Atomic[]>();
            bindings.add(initialBinding);
            Comparator<QueryObject<? extends Axiom>> costComparator=new CostComparator(hermit, bindings, positionInTuple, graph);
            SortedSet<QueryObject<? extends Axiom>> orderedQueryObjects=new TreeSet<QueryObject<? extends Axiom>>(costComparator);
            orderedQueryObjects.addAll(queryObjects);
            for (QueryObject<? extends Axiom> qo : orderedQueryObjects)
                System.out.println(qo);
            for (QueryObject<? extends Axiom> qo : orderedQueryObjects)
                bindings=qo.computeBindings(hermit, graph, bindings, positionInTuple);
            return new HermiTQueryIterator(input,execCxt,bindings,positionInTuple);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return new HermiTQueryIterator(input,execCxt,new ArrayList<Atomic[]>(),new HashMap<Variable,Integer>());
        }
    }
    protected String printNode(Node node) {
        if (node.isURI())
            return "<"+node+">";
        else 
            return node.toString();
    }
}
