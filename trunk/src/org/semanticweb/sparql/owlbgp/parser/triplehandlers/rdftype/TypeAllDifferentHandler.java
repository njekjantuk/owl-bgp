package org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;

public class TypeAllDifferentHandler extends BuiltInTypeHandler {

    public TypeAllDifferentHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_ALL_DIFFERENT.getIRI());
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        consumer.consumeTriple(subject, predicate, object);
    }
}
