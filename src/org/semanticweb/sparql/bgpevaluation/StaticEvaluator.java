package org.semanticweb.sparql.bgpevaluation;

import java.util.List;
import java.util.Map;

import org.semanticweb.HermiT.OWLBGPHermiT;
import org.semanticweb.HermiT.StaticHermiTCostEstimationVisitor;
import org.semanticweb.sparql.arq.OWLOntologyGraph;
import org.semanticweb.sparql.bgpevaluation.monitor.Monitor;
import org.semanticweb.sparql.bgpevaluation.queryobjects.QueryObject;
import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.Variable;
import org.semanticweb.sparql.owlbgp.model.axioms.Axiom;

public class StaticEvaluator extends QueryEvaluator {
    
    protected final StaticCostEstimationVisitor m_costEstimator;
    
    public StaticEvaluator(OWLOntologyGraph graph, Monitor monitor) {
        super(graph, monitor);
        if (m_graph.getReasoner() instanceof OWLBGPHermiT)
            m_costEstimator=new StaticHermiTCostEstimationVisitor(m_graph);
        else 
            m_costEstimator=new StaticCostEstimationVisitor(m_graph);
    }
    
    @Override
    public List<Atomic[]> execute(List<QueryObject<? extends Axiom>> connectedComponent, Map<Variable, Integer> positionInTuple, List<Atomic[]> bindings) {
        List<QueryObject<? extends Axiom>> staticAxiomOrder=StaticQueryReordering.getCheapestOrdering(m_costEstimator, connectedComponent, m_monitor);
        for (QueryObject<? extends Axiom> cheapest : staticAxiomOrder){
            if (!bindings.isEmpty()){
                bindings=cheapest.computeBindings(bindings, positionInTuple);
            }
        }
        return bindings;
    }

}
