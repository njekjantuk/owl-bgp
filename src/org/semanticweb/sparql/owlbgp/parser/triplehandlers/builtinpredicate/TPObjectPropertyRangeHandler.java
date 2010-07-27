package org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.ObjectPropertyRange;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.TriplePredicateHandler;

public class TPObjectPropertyRangeHandler extends TriplePredicateHandler {

    public TPObjectPropertyRangeHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_OBJECT_PROPERTY_RANGE.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        return !consumer.isAnonymous(object);
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        consumer.addAxiom(ObjectPropertyRange.create(consumer.translateObjectPropertyExpression(subject),consumer.translateClassExpression(object)));
        consumer.consumeTriple(subject, predicate, object);
    }
}
