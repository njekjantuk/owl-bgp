package org.semanticweb.sparql.owlbgpparser.triplehandlers;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgpparser.TripleConsumer;

public abstract class TriplePredicateHandler extends AbstractResourceTripleHandler {
    
    protected final Identifier predicateIRI;
    
    public TriplePredicateHandler(TripleConsumer consumer, Identifier predicateIRI) {
        super(consumer);
        this.predicateIRI=predicateIRI;
    }
    public boolean canHandle(Identifier subject, Identifier predicate, Identifier object) {
        return predicate.equals(predicateIRI);
    }
    public Identifier getPredicateIRI() {
        return predicateIRI;
    }
}
