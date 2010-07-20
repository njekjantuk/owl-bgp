package org.semanticweb.sparql.owlbgpparser.triplehandlers.builtinpredicate;

import org.semanticweb.sparql.owlbgp.model.IRI;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.AnnotationPropertyRange;
import org.semanticweb.sparql.owlbgp.model.axioms.DataPropertyRange;
import org.semanticweb.sparql.owlbgp.model.axioms.ObjectPropertyRange;
import org.semanticweb.sparql.owlbgpparser.TripleConsumer;
import org.semanticweb.sparql.owlbgpparser.Vocabulary;
import org.semanticweb.sparql.owlbgpparser.triplehandlers.TriplePredicateHandler;

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
        if (consumer.isObjectPropertyOnly(subject))
            consumer.addAxiom(ObjectPropertyRange.create(consumer.translateObjectPropertyExpression(subject),consumer.translateClassExpression(object)));
        else if (consumer.isDataPropertyOnly(subject))
            consumer.addAxiom(DataPropertyRange.create(consumer.translateDataPropertyExpression(subject),consumer.translateDataRange(object)));
        else if(consumer.isAnnotationProperty(subject))
            consumer.addAxiom(AnnotationPropertyRange.create(consumer.translateAnnotationPropertyExpression(subject), (IRI)object, consumer.getPendingAnnotations()));
        else if (consumer.isDataRange(object))
            // Assume data property
            consumer.addAxiom(DataPropertyRange.create(consumer.translateDataPropertyExpression(subject),consumer.translateDataRange(object)));
        else if (consumer.isClass(object))
            // Assume object property
            consumer.addAxiom(ObjectPropertyRange.create(consumer.translateObjectPropertyExpression(subject),consumer.translateClassExpression(object)));
        else throw new RuntimeException("Could not disambiguate predicate "+predicate+" and object "+object+". ");
        consumer.consumeTriple(subject, predicate, object);
    }
}
