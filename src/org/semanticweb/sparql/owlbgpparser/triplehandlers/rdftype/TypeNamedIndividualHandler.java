package org.semanticweb.sparql.owlbgpparser.triplehandlers.rdftype;

import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.Declaration;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgpparser.TripleConsumer;
import org.semanticweb.sparql.owlbgpparser.Vocabulary;

public class TypeNamedIndividualHandler extends BuiltInTypeHandler {

    public TypeNamedIndividualHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_NAMED_INDIVIDUAL.getIRI());
    }

    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        if (!consumer.isAnonymous(subject)) {
            consumer.addAxiom(Declaration.create((Atomic)consumer.translateIndividual(subject),consumer.getPendingAnnotations()));
            consumer.addIndividual(subject);
            consumer.consumeTriple(subject, predicate, object);
        }
    }
}
