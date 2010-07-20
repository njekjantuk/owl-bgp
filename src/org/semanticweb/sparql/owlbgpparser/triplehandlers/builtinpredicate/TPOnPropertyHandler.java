package org.semanticweb.sparql.owlbgpparser.triplehandlers.builtinpredicate;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgpparser.TripleConsumer;
import org.semanticweb.sparql.owlbgpparser.Vocabulary;
import org.semanticweb.sparql.owlbgpparser.triplehandlers.TriplePredicateHandler;

public class TPOnPropertyHandler extends TriplePredicateHandler {

    public TPOnPropertyHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_ON_PROPERTY.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        consumer.addRestriction(subject);
        return false;   
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
    }
}
