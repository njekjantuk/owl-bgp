package org.semanticweb.sparql.bgpevaluation.queryobjects;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.sparql.bgpevaluation.QueryObjectVisitorEx;
import org.semanticweb.sparql.owlbgp.model.axioms.InverseFunctionalObjectProperty;

public class QO_InverseFunctionalObjectProperty extends QO_ObjectPropertyAxiom<InverseFunctionalObjectProperty> {

    public QO_InverseFunctionalObjectProperty(InverseFunctionalObjectProperty axiomTemplate) {
        super(axiomTemplate);
    }
    protected OWLAxiom getEntailmentAxiom(OWLDataFactory dataFactory, OWLObjectPropertyExpression ope) {
        return dataFactory.getOWLInverseFunctionalObjectPropertyAxiom(ope);
    }
    public <O> O accept(QueryObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
}
