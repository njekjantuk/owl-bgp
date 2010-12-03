package org.semanticweb.sparql.evaluation.queryobjects;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.axioms.FunctionalObjectProperty;

public class QO_FunctionalObjectProperty extends QO_ObjectPropertyAxiom<FunctionalObjectProperty> {

    public QO_FunctionalObjectProperty(FunctionalObjectProperty axiomTemplate) {
	    super(axiomTemplate);
	}
    protected OWLAxiom getEntailmentAxiom(OWLDataFactory dataFactory, OWLObjectPropertyExpression ope) {
        return dataFactory.getOWLFunctionalObjectPropertyAxiom(ope);
    }
}
