package org.semanticweb.sparql.owlbgpparser;

public class TPSomeValuesFromHandler extends TriplePredicateHandler {

    public TPSomeValuesFromHandler(OWLRDFConsumer consumer) {
        super(consumer, OWLRDFVocabulary.OWL_SOME_VALUES_FROM.getIRI());
    }

    public boolean canHandleStreaming(String subject, String predicate, String object) {
        return false;
    }
    public void handleTriple(String subject, String predicate, String object) {
    }
}
