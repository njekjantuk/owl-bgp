package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Identifier;

public abstract class TriplePredicateHandler extends AbstractResourceTripleHandler {
    
    protected final Identifier predicateIRI;
    
    public TriplePredicateHandler(OWLRDFConsumer consumer, Identifier predicateIRI) {
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
