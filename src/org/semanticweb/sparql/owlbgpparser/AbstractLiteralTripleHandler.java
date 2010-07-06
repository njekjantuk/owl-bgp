package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.ILiteral;

public abstract class AbstractLiteralTripleHandler extends AbstractTripleHandler {

    public AbstractLiteralTripleHandler(OWLRDFConsumer consumer) {
        super(consumer);
    }
    public abstract void handleTriple(String subject, String predicate, ILiteral object);

    public abstract boolean canHandle(String subject, String predicate, ILiteral object);

    public abstract boolean canHandleStreaming(String subject, String predicate, ILiteral object);
}
