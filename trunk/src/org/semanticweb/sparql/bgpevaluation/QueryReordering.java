package org.semanticweb.sparql.bgpevaluation;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.semanticweb.sparql.bgpevaluation.queryobjects.QueryObject;
import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.Variable;
import org.semanticweb.sparql.owlbgp.model.axioms.Axiom;

public class QueryReordering {
    public static QueryObject<Axiom>[] reorder(StandardCostEstimator estimator, QueryObject<Axiom>[] atoms, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions) {
        Arrays.sort(atoms,new CostComparator(estimator, candidateBindings, bindingPositions));
        return atoms;
    }
  
	protected static final class CostComparator implements Comparator<QueryObject<? extends Axiom>> {
	    protected final StandardCostEstimator m_estimator;
	    protected final List<Atomic[]> m_candidateBindings;
	    protected final Map<Variable,Integer> m_bindingPositions;
	    
	    public CostComparator(StandardCostEstimator estimator, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions) {
	        m_estimator=estimator;
	        m_candidateBindings=candidateBindings;
	        m_bindingPositions=bindingPositions;
	    }
		public int compare(QueryObject<? extends Axiom> axiomTemplate1, QueryObject<? extends Axiom> axiomTemplate2) {
//		    return m_estimator.getCost(axiomTemplate1.getAxiomTemplate(), m_candidateBindings, m_bindingPositions)-m_estimator.getCost(axiomTemplate1.getAxiomTemplate(), m_candidateBindings, m_bindingPositions);
		    return 0;
		}
	}
}
