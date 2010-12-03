package org.semanticweb.sparql.evaluation.queryobjects;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.axioms.AsymmetricObjectProperty;

public class QO_AsymmetricObjectProperty extends QO_ObjectPropertyAxiom<AsymmetricObjectProperty> {

    public QO_AsymmetricObjectProperty(AsymmetricObjectProperty axiomTemplate) {
	    super(axiomTemplate);
	}
    protected OWLAxiom getEntailmentAxiom(OWLDataFactory dataFactory, OWLObjectPropertyExpression ope) {
        return dataFactory.getOWLAsymmetricObjectPropertyAxiom(ope);
    }
}
