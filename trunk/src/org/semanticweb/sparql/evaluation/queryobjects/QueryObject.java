package org.semanticweb.sparql.evaluation.queryobjects;

import java.util.List;
import java.util.Map;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.sparql.arq.HermiTGraph;
import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.Variable;

public interface QueryObject<T> {
    public T getAxiomTemplate();
	public List<Atomic[]> computeBindings(Reasoner reasoner, HermiTGraph graph, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions);
	public int getCurrentCost(Reasoner reasoner, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions, HermiTGraph graph);
}
