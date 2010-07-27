package org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.TriplePredicateHandler;

public class TPRestHandler extends TriplePredicateHandler {

    public TPRestHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.RDF_REST.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        return true;
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        if (!object.equals(Vocabulary.RDF_NIL.getIRI())) consumer.addRest(subject, object);
        consumer.consumeTriple(subject, predicate, object);
    }
}
