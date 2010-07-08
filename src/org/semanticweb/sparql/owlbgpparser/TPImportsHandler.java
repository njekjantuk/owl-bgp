package org.semanticweb.sparql.owlbgpparser;

public class TPImportsHandler extends TriplePredicateHandler {

    public TPImportsHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_IMPORTS.getIRI());
    }

    public boolean canHandleStreaming(String subject, String predicate, String object) {
        return true;
    }
    public void handleTriple(String subject, String predicate, String object) {
        consumeTriple(subject, predicate, object);
    }
}
