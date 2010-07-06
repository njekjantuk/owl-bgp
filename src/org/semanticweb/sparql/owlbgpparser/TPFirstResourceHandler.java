package org.semanticweb.sparql.owlbgpparser;

public class TPFirstResourceHandler extends TriplePredicateHandler {

    public TPFirstResourceHandler(OWLRDFConsumer consumer) {
        super(consumer, OWLRDFVocabulary.RDF_FIRST.getIRI());
    }

    public boolean canHandleStreaming(String subject, String predicate, String object) {
        return true;
    }
    public void handleTriple(String subject, String predicate, String object) {
        consumer.addFirst(subject, object);
        consumeTriple(subject, predicate, object);
    }
}
