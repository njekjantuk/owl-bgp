package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.ObjectPropertyRange;

public class TPObjectPropertyRangeHandler extends TriplePredicateHandler {

    public TPObjectPropertyRangeHandler(OWLRDFConsumer consumer) {
        super(consumer, OWLRDFVocabulary.OWL_OBJECT_PROPERTY_RANGE.getIRI());
    }

    public boolean canHandleStreaming(String subject, String predicate, String object) {
        return !consumer.isAnonymousNode(object);
    }
    public void handleTriple(String subject, String predicate, String object) {
        addAxiom(ObjectPropertyRange.create(translateObjectProperty(subject),translateClassExpression(object)));
        consumeTriple(subject, predicate, object);
    }
}
