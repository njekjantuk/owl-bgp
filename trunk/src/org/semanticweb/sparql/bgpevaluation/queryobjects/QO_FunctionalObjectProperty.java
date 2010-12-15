package org.semanticweb.sparql.bgpevaluation.queryobjects;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.sparql.arq.OWLOntologyGraph;
import org.semanticweb.sparql.owlbgp.model.axioms.FunctionalObjectProperty;

public class QO_FunctionalObjectProperty extends QO_ObjectPropertyAxiom<FunctionalObjectProperty> {

    public QO_FunctionalObjectProperty(FunctionalObjectProperty axiomTemplate, OWLOntologyGraph graph) {
        super(axiomTemplate, graph);
    }
    
    protected OWLAxiom getEntailmentAxiom(OWLObjectPropertyExpression ope) {
        return m_dataFactory.getOWLFunctionalObjectPropertyAxiom(ope);
    }
    public <O> O accept(QueryObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
}
