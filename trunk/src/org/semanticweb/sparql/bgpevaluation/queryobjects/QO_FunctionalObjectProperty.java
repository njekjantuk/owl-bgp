package org.semanticweb.sparql.bgpevaluation.queryobjects;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.sparql.bgpevaluation.QueryObjectVisitorEx;
import org.semanticweb.sparql.owlbgp.model.axioms.FunctionalObjectProperty;

public class QO_FunctionalObjectProperty extends QO_ObjectPropertyAxiom<FunctionalObjectProperty> {

    public QO_FunctionalObjectProperty(FunctionalObjectProperty axiomTemplate) {
	    super(axiomTemplate);
	}
    protected OWLAxiom getEntailmentAxiom(OWLDataFactory dataFactory, OWLObjectPropertyExpression ope) {
        return dataFactory.getOWLFunctionalObjectPropertyAxiom(ope);
    }
    public <O> O accept(QueryObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
}
