package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.ObjectPropertyRange;

public class TPObjectPropertyRangeHandler extends TriplePredicateHandler {

    public TPObjectPropertyRangeHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_OBJECT_PROPERTY_RANGE.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        return !consumer.isAnonymousNode(object);
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        addAxiom(ObjectPropertyRange.create(translateObjectProperty(subject),translateClassExpression(object)));
        consumeTriple(subject, predicate, object);
    }
}
