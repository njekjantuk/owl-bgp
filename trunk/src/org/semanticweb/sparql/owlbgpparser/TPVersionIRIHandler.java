package org.semanticweb.sparql.owlbgpparser;

public class TPVersionIRIHandler extends TriplePredicateHandler {

    public TPVersionIRIHandler(OWLRDFConsumer consumer) {
        super(consumer, OWLRDFVocabulary.OWL_VERSION_IRI.getIRI());
    }

    public void handleTriple(String subject,String predicate,String object) {
        consumeTriple(subject, predicate, object);
    }
    public boolean canHandleStreaming(String subject,String predicate,String object) {
        // Always apply at the end
        return false;
    }
    public boolean canHandle(String subject, String predicate, String object) {
        return subject.equals(consumer.ontologyIRI);
    }
}
