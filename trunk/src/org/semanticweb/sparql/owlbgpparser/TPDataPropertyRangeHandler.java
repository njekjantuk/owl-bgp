package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.DataPropertyRange;
import org.semanticweb.sparql.owlbgp.model.Identifier;

public class TPDataPropertyRangeHandler extends TriplePredicateHandler {

    public TPDataPropertyRangeHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_DATA_PROPERTY_RANGE.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        return !consumer.isAnonymousNode(object);
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        addAxiom(DataPropertyRange.create(translateDataProperty(subject),translateDataRange(object)));
        consumeTriple(subject, predicate, object);
    }
}
