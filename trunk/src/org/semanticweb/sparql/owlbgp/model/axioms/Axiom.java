package org.semanticweb.sparql.owlbgp.model.axioms;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Annotation;
import org.semanticweb.sparql.owlbgp.model.ExtendedOWLObject;

public interface Axiom extends ExtendedOWLObject {
    public Axiom getAxiomWithoutAnnotations();
    public Set<Annotation> getAnnotations();
}
