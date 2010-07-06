package org.semanticweb.sparql.owlbgpparser;

public class GTPAnnotationResourceTripleHandler extends AbstractResourceTripleHandler {
    public GTPAnnotationResourceTripleHandler(OWLRDFConsumer consumer) {
        super(consumer);
    }
    public boolean canHandleStreaming(String subject, String predicate, String object) {
        return !consumer.isAnonymousNode(subject) && consumer.isAnnotationProperty(predicate);
    }
    public boolean canHandle(String subject, String predicate, String object) {
        return consumer.isAnnotationProperty(predicate) || consumer.isOntology(subject);
    }
    public void handleTriple(String subject, String predicate, String object) {
        consumer.hasAnnotations=true;
    }
}
