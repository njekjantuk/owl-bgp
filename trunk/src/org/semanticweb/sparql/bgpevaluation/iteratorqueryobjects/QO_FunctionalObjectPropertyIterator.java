package org.semanticweb.sparql.bgpevaluation.iteratorqueryobjects;

import java.util.Iterator;
import java.util.Map;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.sparql.arq.OWLOntologyGraph;
import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.Variable;
import org.semanticweb.sparql.owlbgp.model.axioms.FunctionalObjectProperty;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;

public class QO_FunctionalObjectPropertyIterator extends IQO_ObjectPropertyAxiom<FunctionalObjectProperty> implements Iterable<Atomic[]>, Iterator<Atomic[]> {

    public QO_FunctionalObjectPropertyIterator(FunctionalObjectProperty axiomTemplate, OWLOntologyGraph graph, Map<Variable,Integer> bindingPositions) {
        super(axiomTemplate, graph);
        m_toTestObjectProperties.addAll(m_graph.getToTestFunctionalObjectProperties());
        m_knownResults.addAll(m_graph.getKnownFunctionalObjectProperties());
    }
    protected OWLAxiom getEntailmentAxiom(OWLObjectPropertyExpression ope) {
        return m_dataFactory.getOWLFunctionalObjectPropertyAxiom(ope);
    }
    protected ObjectPropertyExpression getObjectPropertyExpression() {
        return m_axiomTemplate.getObjectPropertyExpression();
    }
}
