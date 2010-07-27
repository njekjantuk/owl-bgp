package org.semanticweb.sparql.owlbgp.parser.triplehandlers.general;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Annotation;
import org.semanticweb.sparql.owlbgp.model.AnnotationSubject;
import org.semanticweb.sparql.owlbgp.model.AnnotationValue;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.AnnotationAssertion;
import org.semanticweb.sparql.owlbgp.model.literals.Literal;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.AbstractLiteralTripleHandler;

public class GTPAnnotationLiteralHandler extends AbstractLiteralTripleHandler {

    public GTPAnnotationLiteralHandler(TripleConsumer consumer) {
        super(consumer);
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Literal object) {
        return false;
    }
    public boolean canHandle(Identifier subject, Identifier predicate, Literal object) {
        return !consumer.isAxiom(subject) && consumer.isAnnotationProperty(predicate);
    }
    public void handleTriple(Identifier subject, Identifier predicate, Literal object) {
        Set<Annotation> annos=consumer.getAnnotations(subject, predicate, object, Vocabulary.OWL_ANNOTATION.getIRI());
        consumer.consumeTriple(subject, predicate, object);
        if (consumer.isOntology(subject))
            consumer.addOntologyAnnotation(Annotation.create(consumer.translateAnnotationPropertyExpression(predicate), object,annos));
        else
            consumer.addAxiom(AnnotationAssertion.create(consumer.translateAnnotationPropertyExpression(predicate), (AnnotationSubject)subject, (AnnotationValue)object,annos));
    }
}
