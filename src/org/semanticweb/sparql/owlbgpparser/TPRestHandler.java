package org.semanticweb.sparql.owlbgpparser;

public class TPRestHandler extends TriplePredicateHandler {

    protected  static int count = 0;

    public TPRestHandler(OWLRDFConsumer consumer) {
        super(consumer, OWLRDFVocabulary.RDF_REST.getIRI());
    }

    public boolean canHandleStreaming(String subject, String predicate, String object) {
        return true;
    }
    public void handleTriple(String subject, String predicate, String object) {
        if (!object.equals(OWLRDFVocabulary.RDF_NIL.getIRI())) 
            getConsumer().addRest(subject, object);
        else
            count++;
        consumeTriple(subject, predicate, object);
    }
}
