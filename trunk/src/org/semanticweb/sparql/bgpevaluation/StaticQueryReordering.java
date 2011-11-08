package org.semanticweb.sparql.bgpevaluation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.semanticweb.sparql.bgpevaluation.monitor.Monitor;
import org.semanticweb.sparql.bgpevaluation.queryobjects.QueryObject;
import org.semanticweb.sparql.owlbgp.model.Variable;
import org.semanticweb.sparql.owlbgp.model.axioms.Axiom;

public class StaticQueryReordering {
	
	
	public static List<QueryObject<? extends Axiom>> getCheapestOrdering(StaticCostEstimationVisitor estimator, List<QueryObject<? extends Axiom>> atoms, Monitor monitor) {
        List<QueryObject<? extends Axiom>> cheapestOrder=new ArrayList<QueryObject<? extends Axiom>>();
        Set<Variable> bound=new HashSet<Variable>();
        int size=atoms.size();
        double cheapestCost=0;
        boolean first=true;
        for (int y=0;y<size;y++) {
          cheapestCost=0;
          first=true;
          QueryObject<? extends Axiom> cheapestAtom=null;
          for (QueryObject<? extends Axiom> qo : atoms) {
            monitor.costEvaluationStarted(qo);
            double[] costs=qo.accept(estimator,bound);
            monitor.costEvaluationFinished(costs[0], costs[1]);
            double totalCost=costs[0]+costs[1];
            if (first || totalCost<cheapestCost) {
                first=false;
            	cheapestCost=totalCost;
                cheapestAtom=qo;
            }
          }
          cheapestOrder.add(cheapestAtom);          
          bound.addAll(cheapestAtom.getAxiomTemplate().getVariablesInSignature());
          atoms.remove(cheapestAtom);
          
        }  
        return cheapestOrder;
    }
}
