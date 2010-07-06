package org.semanticweb.sparql.owlbgpparser;

public class TPAnnotatedSourceHandler extends TriplePredicateHandler {

    public TPAnnotatedSourceHandler(OWLRDFConsumer consumer) {
        super(consumer, OWLRDFVocabulary.OWL_ANNOTATED_SOURCE.getIRI());
    }

    public boolean canHandleStreaming(String subject, String predicate, String object) {
        getConsumer().addAnnotatedSource(object, subject);
        return false;
    }

    public void handleTriple(String subject, String predicate, String object) {
    }
}
