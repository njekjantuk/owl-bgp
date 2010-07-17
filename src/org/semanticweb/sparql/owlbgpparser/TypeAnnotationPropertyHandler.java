package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.Declaration;
import org.semanticweb.sparql.owlbgp.model.Identifier;

public class TypeAnnotationPropertyHandler extends BuiltInTypeHandler{

    public TypeAnnotationPropertyHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_ANNOTATION_PROPERTY.getIRI());
    }

    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        if (!consumer.isAnonymousNode(subject)) {
            addAxiom(Declaration.create((Atomic)consumer.translateAnnotationPropertyExpression(subject),consumer.getPendingAnnotations()));
            consumer.addAnnotationProperty(subject);
            consumeTriple(subject, predicate, object);
        }
    }
}
