package org.semanticweb.sparql.bgpevaluation.iteratorqueryobjects;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.sparql.arq.OWLOntologyGraph;
import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.ToOWLAPIConverter;
import org.semanticweb.sparql.owlbgp.model.Variable;
import org.semanticweb.sparql.owlbgp.model.axioms.Axiom;

public abstract class AbstractIteratorQueryObject<T extends Axiom> implements IteratorQueryObject<T> {
    
    protected final OWLOntologyGraph m_graph;
    protected final OWLReasoner m_reasoner;
    protected final OWLDataFactory m_dataFactory;
    protected final ToOWLAPIConverter m_toOWLAPIConverter;
    protected final T m_axiomTemplate;
    protected final Set<Variable> m_variablesInTemplate;
    protected final Set<Variable> m_unboundVariables;
    protected Map<Variable,Integer> m_bindingPositions;
    protected int m_resultIndex;
    protected List<Atomic[]> m_currentBindings;
    protected int m_lastTestedIndex;

    public AbstractIteratorQueryObject(T axiomTemplate, OWLOntologyGraph graph) {
        m_graph=graph;
        m_reasoner=m_graph.getReasoner();
        m_dataFactory=m_reasoner.getRootOntology().getOWLOntologyManager().getOWLDataFactory();
        m_toOWLAPIConverter=new ToOWLAPIConverter(m_dataFactory);
        m_axiomTemplate=axiomTemplate;
        m_variablesInTemplate=m_axiomTemplate.getVariablesInSignature();
        m_unboundVariables=new HashSet<Variable>();
    }
    public T getAxiomTemplate() {
        return m_axiomTemplate;
    }
    public void setExistingBindings(List<Atomic[]> currentBindings, Map<Variable,Integer> bindingPositions) {
        m_unboundVariables.clear();
        m_currentBindings=currentBindings;
        m_bindingPositions=bindingPositions;
        for (Variable var : m_variablesInTemplate)
            if (!bindingPositions.containsKey(var))
                m_unboundVariables.add(var);
        m_lastTestedIndex=currentBindings.size();
        m_resultIndex=currentBindings.size()-1;
    }
    public String toString() {
        return m_axiomTemplate.toString();
    }
}
