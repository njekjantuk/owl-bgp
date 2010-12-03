package org.semanticweb.sparql.evaluation.queryobjects;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.axioms.SymmetricObjectProperty;

public class QO_SymmetricObjectProperty extends QO_ObjectPropertyAxiom<SymmetricObjectProperty> {

    public QO_SymmetricObjectProperty(SymmetricObjectProperty axiomTemplate) {
	    super(axiomTemplate);
	}
    protected OWLAxiom getEntailmentAxiom(OWLDataFactory dataFactory, OWLObjectPropertyExpression ope) {
        return dataFactory.getOWLSymmetricObjectPropertyAxiom(ope);
    }
}
