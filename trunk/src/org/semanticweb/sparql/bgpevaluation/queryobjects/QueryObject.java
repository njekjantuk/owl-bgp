package org.semanticweb.sparql.bgpevaluation.queryobjects;

import java.util.List;
import java.util.Map;

import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.sparql.arq.OWLOntologyGraph;
import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.Variable;

public interface QueryObject<T> {
    public T getAxiomTemplate();
	public List<Atomic[]> computeBindings(OWLReasoner reasoner, OWLOntologyGraph graph, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions);
//	public int getCurrentCost(Reasoner reasoner, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions, OWLOntologyGraph graph);
}
