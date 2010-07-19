package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Annotation;
import org.semanticweb.sparql.owlbgp.model.AnnotationSubject;
import org.semanticweb.sparql.owlbgp.model.AnnotationValue;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.AnnotationAssertion;
import org.semanticweb.sparql.owlbgp.model.literals.Literal;

public class GTPAnnotationLiteralHandler extends AbstractLiteralTripleHandler {

    public GTPAnnotationLiteralHandler(OWLRDFConsumer consumer) {
        super(consumer);
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Literal object) {
        return !consumer.isAnonymousNode(subject) && !consumer.isAnnotation(subject) && consumer.isAnnotationProperty(predicate);
    }
    public boolean canHandle(Identifier subject, Identifier predicate, Literal object) {
        boolean axiom=consumer.isAxiom(subject);
        boolean annotation=consumer.isAnnotation(subject);
        return !axiom && !annotation && consumer.isAnnotationProperty(predicate);
    }
    public void handleTriple(Identifier subject, Identifier predicate, Literal object) {
        consumeTriple(subject, predicate, object);
        if (consumer.isOntology(subject)) {
            consumer.addOntologyAnnotation(Annotation.create(consumer.translateAnnotationPropertyExpression(predicate), object));
        }
        else {
            AnnotationAssertion ax=AnnotationAssertion.create(consumer.translateAnnotationPropertyExpression(predicate), (AnnotationSubject)subject, (AnnotationValue)object, consumer.getPendingAnnotations());
            consumer.addAxiom(ax);
        }
    }
}
