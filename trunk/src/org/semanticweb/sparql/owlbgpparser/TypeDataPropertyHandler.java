package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.Declaration;
import org.semanticweb.sparql.owlbgp.model.Identifier;

public class TypeDataPropertyHandler extends BuiltInTypeHandler {

    public TypeDataPropertyHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_DATA_PROPERTY.getIRI());
    }

    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        if (!consumer.isAnonymousNode(subject)) {
            addAxiom(Declaration.create((Atomic)consumer.translateDataPropertyExpression(subject),consumer.getPendingAnnotations()));
            consumer.addDataProperty(subject);
            consumeTriple(subject, predicate, object);
        }
    }
}
