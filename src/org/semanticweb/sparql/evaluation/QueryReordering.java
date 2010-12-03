package org.semanticweb.sparql.evaluation;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.sparql.evaluation.queryobjects.QueryObject;
import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.Variable;
import org.semanticweb.sparql.owlbgp.model.axioms.Axiom;

public class QueryReordering {
    public static QueryObject<Axiom>[] reorder(QueryObject<Axiom>[] atoms, Reasoner reasoner, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions, HermiTGraph graph) {
        Arrays.sort(atoms,new CostComparator(reasoner, candidateBindings, bindingPositions, graph));
        return atoms;
    }
  
	protected static final class CostComparator implements Comparator<QueryObject<? extends Axiom>> {
	    protected final Reasoner m_reasoner;
	    protected final List<Atomic[]> m_candidateBindings;
	    protected final Map<Variable,Integer> m_bindingPositions;
	    protected final HermiTGraph m_graph;
	    
	    public CostComparator(Reasoner reasoner, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions, HermiTGraph graph) {
	        m_reasoner=reasoner;
	        m_candidateBindings=candidateBindings;
	        m_bindingPositions=bindingPositions;
	        m_graph=graph;
	    }
		public int compare(QueryObject<? extends Axiom> axiomTemplate1, QueryObject<? extends Axiom> axiomTemplate2) {
			return axiomTemplate1.getCurrentCost(m_reasoner, m_candidateBindings, m_bindingPositions, m_graph)-axiomTemplate2.getCurrentCost(m_reasoner, m_candidateBindings, m_bindingPositions, m_graph);
		}
	}
}
