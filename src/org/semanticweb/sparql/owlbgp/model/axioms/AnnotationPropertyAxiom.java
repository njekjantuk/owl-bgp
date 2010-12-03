package org.semanticweb.sparql.owlbgp.model.axioms;

import org.semanticweb.sparql.owlbgp.model.properties.AnnotationPropertyExpression;

public interface AnnotationPropertyAxiom extends Axiom {
    public AnnotationPropertyExpression getAnnotationPropertyExpression();
}
