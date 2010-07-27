package org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype;

import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.Declaration;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;

public class TypeClassHandler extends BuiltInTypeHandler {

    public TypeClassHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_CLASS.getIRI());
    }

    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        if (!consumer.isAnonymous(subject)) {
            consumer.addAxiom(Declaration.create((Atomic)consumer.translateClassExpression(subject)));
            consumer.addClass(subject);
            consumer.consumeTriple(subject, predicate, object);
        }
    }
}
