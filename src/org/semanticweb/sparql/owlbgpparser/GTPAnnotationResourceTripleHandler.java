package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Annotation;
import org.semanticweb.sparql.owlbgp.model.AnnotationAssertion;
import org.semanticweb.sparql.owlbgp.model.AnnotationPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.AnnotationSubject;
import org.semanticweb.sparql.owlbgp.model.AnnotationValue;
import org.semanticweb.sparql.owlbgp.model.AnonymousIndividual;
import org.semanticweb.sparql.owlbgp.model.IRI;
import org.semanticweb.sparql.owlbgp.model.Identifier;

public class GTPAnnotationResourceTripleHandler extends AbstractResourceTripleHandler {
    public GTPAnnotationResourceTripleHandler(OWLRDFConsumer consumer) {
        super(consumer);
    }
    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        return !consumer.isAnonymousNode(subject) && consumer.isAnnotationProperty(predicate);
    }
    public boolean canHandle(Identifier subject, Identifier predicate, Identifier object) {
        return consumer.isAnnotationProperty(predicate) || consumer.isOntology(subject);
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        AnnotationValue value;
        if (consumer.isAnonymousNode(object))
            value=(AnonymousIndividual)object;
        else value=(IRI)object;
        AnnotationPropertyExpression prop=consumer.translateAnnotationPropertyExpression(predicate);
        Annotation anno=Annotation.create(prop, value);
        if (consumer.isOntology(subject)) consumer.addOntologyAnnotation(anno);
        else addAxiom(AnnotationAssertion.create(anno.getAnnotationProperty(),(AnnotationSubject)subject, anno.getAnnotationValue(), consumer.getPendingAnnotations()));
    }
}
