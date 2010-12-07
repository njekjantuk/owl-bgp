package org.semanticweb.sparql.bgpevaluation.queryobjects;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.axioms.ReflexiveObjectProperty;

public class QO_ReflexiveObjectProperty extends QO_ObjectPropertyAxiom<ReflexiveObjectProperty> {

    public QO_ReflexiveObjectProperty(ReflexiveObjectProperty axiomTemplate) {
	    super(axiomTemplate);
	}
    protected OWLAxiom getEntailmentAxiom(OWLDataFactory dataFactory, OWLObjectPropertyExpression ope) {
        return dataFactory.getOWLReflexiveObjectPropertyAxiom(ope);
    }
}
