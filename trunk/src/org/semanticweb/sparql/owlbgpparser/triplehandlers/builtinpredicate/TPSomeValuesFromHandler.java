package org.semanticweb.sparql.owlbgpparser.triplehandlers.builtinpredicate;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgpparser.TripleConsumer;
import org.semanticweb.sparql.owlbgpparser.Vocabulary;
import org.semanticweb.sparql.owlbgpparser.triplehandlers.TriplePredicateHandler;

public class TPSomeValuesFromHandler extends TriplePredicateHandler {

    public TPSomeValuesFromHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_SOME_VALUES_FROM.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        consumer.addRestriction(subject);
        return false;
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
    }
}
