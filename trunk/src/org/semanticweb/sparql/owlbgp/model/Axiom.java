package org.semanticweb.sparql.owlbgp.model;

import java.util.Set;

public interface Axiom extends ExtendedOWLObject {
    public Set<Annotation> getAnnotations();
}
