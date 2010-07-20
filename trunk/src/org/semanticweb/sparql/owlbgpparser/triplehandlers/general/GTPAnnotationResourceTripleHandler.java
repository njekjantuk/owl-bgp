package org.semanticweb.sparql.owlbgpparser.triplehandlers.general;

import org.semanticweb.sparql.owlbgp.model.Annotation;
import org.semanticweb.sparql.owlbgp.model.AnnotationSubject;
import org.semanticweb.sparql.owlbgp.model.AnnotationValue;
import org.semanticweb.sparql.owlbgp.model.IRI;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.AnnotationAssertion;
import org.semanticweb.sparql.owlbgp.model.individuals.AnonymousIndividual;
import org.semanticweb.sparql.owlbgpparser.TripleConsumer;
import org.semanticweb.sparql.owlbgpparser.triplehandlers.AbstractResourceTripleHandler;

public class GTPAnnotationResourceTripleHandler extends AbstractResourceTripleHandler {
    public GTPAnnotationResourceTripleHandler(TripleConsumer consumer) {
        super(consumer);
    }
    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        return !consumer.isAnonymous(subject) && consumer.isAnnotationProperty(predicate);
    }
    public boolean canHandle(Identifier subject, Identifier predicate, Identifier object) {
        return consumer.isAnnotationProperty(predicate) || consumer.isOntology(subject);
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        AnnotationValue value=(consumer.isAnonymous(object) ? (AnonymousIndividual)object : (IRI)object);
        Annotation anno=Annotation.create(consumer.translateAnnotationPropertyExpression(predicate), value);
        if (consumer.isOntology(subject)) 
            consumer.addOntologyAnnotation(anno);
        else 
            consumer.addAxiom(AnnotationAssertion.create(anno.getAnnotationProperty(),(AnnotationSubject)subject, anno.getAnnotationValue(), consumer.getPendingAnnotations()));
    }
}
