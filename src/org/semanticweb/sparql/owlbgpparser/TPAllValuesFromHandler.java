package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Identifier;

public class TPAllValuesFromHandler extends TriplePredicateHandler {

    public TPAllValuesFromHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_ALL_VALUES_FROM.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        consumer.addRestriction(subject);
        return false;
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
    }
}
