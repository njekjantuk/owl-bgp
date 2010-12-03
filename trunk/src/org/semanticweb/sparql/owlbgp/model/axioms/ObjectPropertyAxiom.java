package org.semanticweb.sparql.owlbgp.model.axioms;

import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;

public interface ObjectPropertyAxiom extends Axiom {
    public ObjectPropertyExpression getObjectPropertyExpression();
}
