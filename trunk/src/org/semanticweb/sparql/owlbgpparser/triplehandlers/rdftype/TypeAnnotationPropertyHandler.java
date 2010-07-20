package org.semanticweb.sparql.owlbgpparser.triplehandlers.rdftype;

import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.Declaration;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgpparser.TripleConsumer;
import org.semanticweb.sparql.owlbgpparser.Vocabulary;

public class TypeAnnotationPropertyHandler extends BuiltInTypeHandler{

    public TypeAnnotationPropertyHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_ANNOTATION_PROPERTY.getIRI());
    }

    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        if (!consumer.isAnonymous(subject)) {
            consumer.addAxiom(Declaration.create((Atomic)consumer.translateAnnotationPropertyExpression(subject),consumer.getPendingAnnotations()));
            consumer.addAnnotationProperty(subject);
            consumer.consumeTriple(subject, predicate, object);
        }
    }
}
