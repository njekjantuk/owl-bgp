package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.literals.Literal;

public abstract class AbstractLiteralTripleHandler extends AbstractTripleHandler {

    public AbstractLiteralTripleHandler(OWLRDFConsumer consumer) {
        super(consumer);
    }
    public abstract void handleTriple(Identifier subject, Identifier predicate, Literal object);

    public abstract boolean canHandle(Identifier subject, Identifier predicate, Literal object);

    public abstract boolean canHandleStreaming(Identifier subject, Identifier predicate, Literal object);
}
