package org.semanticweb.sparql.owlbgp.parser.triplehandlers;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;

public abstract class AbstractResourceTripleHandler {
    
    protected final TripleConsumer consumer;
    
    public AbstractResourceTripleHandler(TripleConsumer consumer) {
        this.consumer=consumer;
    }
    public boolean isSubjectOrObjectAnonymous(Identifier subject, Identifier object) {
        return consumer.isAnonymous(subject) || consumer.isAnonymous(object);
    }
    public abstract void handleTriple(Identifier subject, Identifier predicate, Identifier object);
    public abstract boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object);
    public abstract boolean canHandle(Identifier subject, Identifier predicate, Identifier object);
}
