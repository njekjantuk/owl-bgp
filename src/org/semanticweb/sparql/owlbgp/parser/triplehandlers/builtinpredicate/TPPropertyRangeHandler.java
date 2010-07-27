package org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Annotation;
import org.semanticweb.sparql.owlbgp.model.IRI;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.AnnotationPropertyRange;
import org.semanticweb.sparql.owlbgp.model.axioms.DataPropertyRange;
import org.semanticweb.sparql.owlbgp.model.axioms.ObjectPropertyRange;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.TriplePredicateHandler;

public class TPPropertyRangeHandler extends TriplePredicateHandler {

    public TPPropertyRangeHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.RDFS_RANGE.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        if (!consumer.isAnonymous(object)) {
            if (consumer.isObjectPropertyOnly(subject)) return true;
            else if (consumer.isDataPropertyOnly(subject)) return true;
        }
        return false;
    }
    public void handleTriple(Identifier subject,Identifier predicate,Identifier object) {
        Set<Annotation> annos=consumer.getAnnotations(subject, predicate, object, Vocabulary.OWL_AXIOM.getIRI());
        if (consumer.isObjectPropertyOnly(subject))
            consumer.addAxiom(ObjectPropertyRange.create(consumer.translateObjectPropertyExpression(subject),consumer.translateClassExpression(object),annos));
        else if (consumer.isDataPropertyOnly(subject))
            consumer.addAxiom(DataPropertyRange.create(consumer.translateDataPropertyExpression(subject),consumer.translateDataRange(object),annos));
        else if(consumer.isAnnotationProperty(subject))
            consumer.addAxiom(AnnotationPropertyRange.create(consumer.translateAnnotationPropertyExpression(subject),(IRI)object,annos));
        else if (consumer.isDataRange(object))
            // Assume data property
            consumer.addAxiom(DataPropertyRange.create(consumer.translateDataPropertyExpression(subject),consumer.translateDataRange(object),annos));
        else if (consumer.isClass(object))
            // Assume object property
            consumer.addAxiom(ObjectPropertyRange.create(consumer.translateObjectPropertyExpression(subject),consumer.translateClassExpression(object),annos));
        else throw new RuntimeException("Could not disambiguate predicate "+predicate+" and object "+object+". ");
        consumer.consumeTriple(subject, predicate, object);
    }
}
