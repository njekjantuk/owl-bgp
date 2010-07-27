package org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype;

import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.Declaration;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;

public class TypeNamedIndividualHandler extends BuiltInTypeHandler {

    public TypeNamedIndividualHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_NAMED_INDIVIDUAL.getIRI());
    }

    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        consumer.addAxiom(Declaration.create((Atomic)consumer.translateIndividual(subject)));
        consumer.addIndividual(subject);
        consumer.consumeTriple(subject, predicate, object);
    }
}
