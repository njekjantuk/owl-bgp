package org.semanticweb.sparql.owlbgp.parser.triplehandlers.general;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Annotation;
import org.semanticweb.sparql.owlbgp.model.AnnotationSubject;
import org.semanticweb.sparql.owlbgp.model.AnnotationValue;
import org.semanticweb.sparql.owlbgp.model.IRI;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.AnnotationAssertion;
import org.semanticweb.sparql.owlbgp.model.individuals.AnonymousIndividual;
import org.semanticweb.sparql.owlbgp.model.properties.AnnotationPropertyExpression;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.AbstractResourceTripleHandler;

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
        Set<Annotation> annos=consumer.getAnnotations(subject, predicate, object, Vocabulary.OWL_ANNOTATION.getIRI());
        AnnotationValue value=(consumer.isAnonymous(object) ? (AnonymousIndividual)object : (IRI)object);
        AnnotationPropertyExpression prop=consumer.translateAnnotationPropertyExpression(predicate);
        if (consumer.isOntology(subject)) 
            consumer.addOntologyAnnotation(Annotation.create(prop,value,annos));
        else 
            consumer.addAxiom(AnnotationAssertion.create(prop,(AnnotationSubject)subject,value,annos));
    }
}
