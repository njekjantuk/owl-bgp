package org.semanticweb.sparql.owlbgp.parser.triplehandlers;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.literals.Literal;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;

public abstract class AbstractLiteralTripleHandler {

    protected final TripleConsumer consumer;
    
    public AbstractLiteralTripleHandler(TripleConsumer consumer) {
        this.consumer=consumer;
    }
    public abstract void handleTriple(Identifier subject, Identifier predicate, Literal object);
    public abstract boolean canHandle(Identifier subject, Identifier predicate, Literal object);
    public abstract boolean canHandleStreaming(Identifier subject, Identifier predicate, Literal object);
}
