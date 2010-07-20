package org.semanticweb.sparql.owlbgpparser.triplehandlers.rdftype;

import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.Declaration;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgpparser.TripleConsumer;
import org.semanticweb.sparql.owlbgpparser.Vocabulary;

public class TypeDataPropertyHandler extends BuiltInTypeHandler {

    public TypeDataPropertyHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_DATA_PROPERTY.getIRI());
    }

    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        if (!consumer.isAnonymous(subject)) {
            consumer.addAxiom(Declaration.create((Atomic)consumer.translateDataPropertyExpression(subject),consumer.getPendingAnnotations()));
            consumer.addDataProperty(subject);
            consumer.consumeTriple(subject, predicate, object);
        }
    }
}
