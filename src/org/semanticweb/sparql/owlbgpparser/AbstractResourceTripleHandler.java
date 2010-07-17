package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Identifier;

public abstract class AbstractResourceTripleHandler extends AbstractTripleHandler {
    public AbstractResourceTripleHandler(OWLRDFConsumer consumer) {
        super(consumer);
    }
    protected boolean isSubjectOrObjectAnonymous(Identifier subject, Identifier object) {
        return consumer.isAnonymousNode(subject) || consumer.isAnonymousNode(object);
    }
    public abstract void handleTriple(Identifier subject, Identifier predicate, Identifier object);
    public abstract boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object);
    public abstract boolean canHandle(Identifier subject, Identifier predicate, Identifier object);
}
