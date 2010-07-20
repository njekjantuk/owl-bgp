package org.semanticweb.sparql.owlbgpparser.triplehandlers.builtinpredicate;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.SameIndividual;
import org.semanticweb.sparql.owlbgpparser.TripleConsumer;
import org.semanticweb.sparql.owlbgpparser.Vocabulary;
import org.semanticweb.sparql.owlbgpparser.triplehandlers.TriplePredicateHandler;

public class TPSameAsHandler extends TriplePredicateHandler {

    public TPSameAsHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_SAME_AS.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        return true;
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        consumer.addAxiom(SameIndividual.create(consumer.translateIndividual(subject),consumer.translateIndividual(object)));
        consumer.consumeTriple(subject, predicate, object);
    }
}
