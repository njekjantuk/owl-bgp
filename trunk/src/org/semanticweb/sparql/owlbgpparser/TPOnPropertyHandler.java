package org.semanticweb.sparql.owlbgpparser;

public class TPOnPropertyHandler extends TriplePredicateHandler {

    protected static int count=0;

    public TPOnPropertyHandler(OWLRDFConsumer consumer) {
        super(consumer, OWLRDFVocabulary.OWL_ON_PROPERTY.getIRI());
    }

    public boolean canHandleStreaming(String subject, String predicate, String object) {
        consumer.addRestriction(subject);
        count++;
        return false;   
    }
    public void handleTriple(String subject, String predicate, String object) {
    }
}
