package org.semanticweb.sparql.bgpevaluation.iteratorqueryobjects;

import java.util.Iterator;

import org.semanticweb.sparql.owlbgp.model.Atomic;

public interface IteratorQueryObject<T> extends Iterable<Atomic[]>, Iterator<Atomic[]> {
    public T getAxiomTemplate();
}
