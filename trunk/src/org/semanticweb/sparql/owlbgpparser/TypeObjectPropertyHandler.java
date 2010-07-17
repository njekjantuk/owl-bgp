package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.Declaration;
import org.semanticweb.sparql.owlbgp.model.Identifier;

public class TypeObjectPropertyHandler extends BuiltInTypeHandler {

    public TypeObjectPropertyHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_OBJECT_PROPERTY.getIRI());
    }

    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        if (!consumer.isAnonymousNode(subject)) {
            addAxiom(Declaration.create((Atomic)consumer.translateObjectPropertyExpression(subject),consumer.getPendingAnnotations()));
            consumer.addObjectProperty(subject);
            consumeTriple(subject, predicate, object);
        }
    }
}
