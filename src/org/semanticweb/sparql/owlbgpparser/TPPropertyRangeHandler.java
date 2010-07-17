package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.AnnotationPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.AnnotationPropertyRange;
import org.semanticweb.sparql.owlbgp.model.DataPropertyRange;
import org.semanticweb.sparql.owlbgp.model.IRI;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.ObjectPropertyRange;

public class TPPropertyRangeHandler extends TriplePredicateHandler {

    public TPPropertyRangeHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.RDFS_RANGE.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        if (!consumer.isAnonymousNode(object)) {
            if (consumer.isObjectPropertyOnly(subject)) return true;
            else if (consumer.isDataPropertyOnly(subject)) return true;
        }
        return false;
    }
    public void handleTriple(Identifier subject,Identifier predicate,Identifier object) {
        if (consumer.isObjectPropertyOnly(subject))
            translateObjectPropertyRange(subject, object, predicate);
        else if (consumer.isDataPropertyOnly(subject)) {
            addAxiom(DataPropertyRange.create(translateDataProperty(subject), translateDataRange(object)));
            consumeTriple(subject, predicate, object);
        } else if(consumer.isAnnotationProperty(subject)) {
            AnnotationPropertyExpression prop=consumer.translateAnnotationPropertyExpression(subject);
            consumer.addAxiom(AnnotationPropertyRange.create(prop, (IRI)object, consumer.getPendingAnnotations()));
            consumeTriple(subject, predicate, object);
        } else if (consumer.isDataRange(object)) {
            // Assume data property
            addAxiom(DataPropertyRange.create(translateDataProperty(subject), translateDataRange(object)));
            consumeTriple(subject, predicate, object);
        } else if (consumer.isClass(object)) {
            // Assume object property
            translateObjectPropertyRange(subject, object, predicate);
        } else throw new RuntimeException("Could not disambiguate predicate "+predicate+" and object "+object+". "); 
    }
    protected void translateObjectPropertyRange(Identifier subject,Identifier object,Identifier predicate) {
        addAxiom(ObjectPropertyRange.create(translateObjectProperty(subject), translateClassExpression(object)));
        consumeTriple(subject, predicate, object);
    }
}
