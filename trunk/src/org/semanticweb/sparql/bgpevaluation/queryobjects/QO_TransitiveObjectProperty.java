package org.semanticweb.sparql.bgpevaluation.queryobjects;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.sparql.bgpevaluation.QueryObjectVisitorEx;
import org.semanticweb.sparql.owlbgp.model.axioms.TransitiveObjectProperty;

public class QO_TransitiveObjectProperty extends QO_ObjectPropertyAxiom<TransitiveObjectProperty> {

    public QO_TransitiveObjectProperty(TransitiveObjectProperty axiomTemplate) {
	    super(axiomTemplate);
	}
    protected OWLAxiom getEntailmentAxiom(OWLDataFactory dataFactory, OWLObjectPropertyExpression ope) {
        return dataFactory.getOWLTransitiveObjectPropertyAxiom(ope);
    }
    public <O> O accept(QueryObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
}
