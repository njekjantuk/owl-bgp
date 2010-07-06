package org.semanticweb.sparql.owlbgpparser;

public abstract class AbstractResourceTripleHandler extends AbstractTripleHandler {
    public AbstractResourceTripleHandler(OWLRDFConsumer consumer) {
        super(consumer);
    }
    protected boolean isSubjectOrObjectAnonymous(String subject, String object) {
        return consumer.isAnonymousNode(subject) || consumer.isAnonymousNode(object);
    }
    public abstract void handleTriple(String subject, String predicate, String object);
    public abstract boolean canHandleStreaming(String subject, String predicate, String object);
    public abstract boolean canHandle(String subject, String predicate, String object);
}
