package org.semanticweb.sparql.owlbgpparser.triplehandlers;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.literals.Literal;
import org.semanticweb.sparql.owlbgpparser.TripleConsumer;

public abstract class AbstractLiteralTripleHandler {

    protected final TripleConsumer consumer;
    
    public AbstractLiteralTripleHandler(TripleConsumer consumer) {
        this.consumer=consumer;
    }
    public abstract void handleTriple(Identifier subject, Identifier predicate, Literal object);
    public abstract boolean canHandle(Identifier subject, Identifier predicate, Literal object);
    public abstract boolean canHandleStreaming(Identifier subject, Identifier predicate, Literal object);
}
