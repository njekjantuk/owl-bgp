package org.semanticweb.sparql.owlbgp.parser.triplehandlers;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;

public abstract class TriplePredicateHandler extends AbstractResourceTripleHandler {
    
    protected final Identifier predicateIRI;
    
    public TriplePredicateHandler(TripleConsumer consumer, Identifier predicateIRI) {
        super(consumer);
        this.predicateIRI=predicateIRI;
    }
//    public boolean canHandle(Identifier subject, Identifier predicate, Identifier object) {
//        return predicate.equals(predicateIRI);
//    }
//    public Identifier getPredicateIRI() {
//        return predicateIRI;
//    }
}
