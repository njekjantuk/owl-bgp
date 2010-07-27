package org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.DataPropertyRange;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.TriplePredicateHandler;

public class TPDataPropertyRangeHandler extends TriplePredicateHandler {

    public TPDataPropertyRangeHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_DATA_PROPERTY_RANGE.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        return !consumer.isAnonymous(object);
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        consumer.addAxiom(DataPropertyRange.create(consumer.translateDataPropertyExpression(subject),consumer.translateDataRange(object)));
        consumer.consumeTriple(subject, predicate, object);
    }
}
