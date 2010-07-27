package org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.literals.Literal;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.AbstractLiteralTripleHandler;

public class TPFirstLiteralHandler extends AbstractLiteralTripleHandler {

    public TPFirstLiteralHandler(TripleConsumer consumer) {
        super(consumer);
    }

    public boolean canHandle(Identifier subject, Identifier predicate, Literal object) {
        return predicate.equals(Vocabulary.RDF_FIRST.getIRI());
    }
    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Literal object) {
        return predicate.equals(Vocabulary.RDF_FIRST.getIRI());
    }
    public void handleTriple(Identifier subject, Identifier predicate, Literal object) {
        consumer.addFirst(subject, object);
        consumer.consumeTriple(subject, predicate, object);
    }
}
