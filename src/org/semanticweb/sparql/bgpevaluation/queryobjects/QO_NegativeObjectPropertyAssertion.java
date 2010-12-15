package org.semanticweb.sparql.bgpevaluation.queryobjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.semanticweb.sparql.arq.OWLOntologyGraph;
import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.Variable;
import org.semanticweb.sparql.owlbgp.model.axioms.NegativeObjectPropertyAssertion;

public class QO_NegativeObjectPropertyAssertion  extends AbstractQueryObject<NegativeObjectPropertyAssertion> {

    public QO_NegativeObjectPropertyAssertion(NegativeObjectPropertyAssertion axiomTemplate, OWLOntologyGraph graph) {
        super(axiomTemplate, graph);
    }
    protected List<Atomic[]> addBindings(Atomic[] currentBinding, Map<Variable,Integer> bindingPositions) {
        Map<Variable,Atomic> bindingMap=new HashMap<Variable, Atomic>();
        // apply bindings that are already computed from previous steps
        for (Variable var : bindingPositions.keySet())
            bindingMap.put(var, currentBinding[bindingPositions.get(var)]);
        try {
            NegativeObjectPropertyAssertion assertion=((NegativeObjectPropertyAssertion)m_axiomTemplate.getBoundVersion(bindingMap));
            return complex(currentBinding,assertion,bindingPositions);
        } catch (IllegalArgumentException e) {
            // current binding is incompatible will not add new bindings in newBindings
            return new ArrayList<Atomic[]>();
        }
    }
    public <O> O accept(QueryObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
}
