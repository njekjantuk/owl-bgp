package org.semanticweb.sparql.evaluation.queryobjects;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.axioms.IrreflexiveObjectProperty;

public class QO_IrreflexiveObjectProperty extends QO_ObjectPropertyAxiom<IrreflexiveObjectProperty> {

    public QO_IrreflexiveObjectProperty(IrreflexiveObjectProperty axiomTemplate) {
	    super(axiomTemplate);
	}
    protected OWLAxiom getEntailmentAxiom(OWLDataFactory dataFactory, OWLObjectPropertyExpression ope) {
        return dataFactory.getOWLIrreflexiveObjectPropertyAxiom(ope);
    }
}
