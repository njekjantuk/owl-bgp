package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.DataPropertyRange;

public class TPDataPropertyRangeHandler extends TriplePredicateHandler {

    public TPDataPropertyRangeHandler(OWLRDFConsumer consumer) {
        super(consumer, OWLRDFVocabulary.OWL_DATA_PROPERTY_RANGE.getIRI());
    }

    public boolean canHandleStreaming(String subject, String predicate, String object) {
        return !consumer.isAnonymousNode(object);
    }
    public void handleTriple(String subject, String predicate, String object) {
        addAxiom(DataPropertyRange.create(translateDataProperty(subject),translateDataRange(object)));
        consumeTriple(subject, predicate, object);
    }
}
