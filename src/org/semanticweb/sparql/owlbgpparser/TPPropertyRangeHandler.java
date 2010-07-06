package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.DataPropertyRange;
import org.semanticweb.sparql.owlbgp.model.ObjectPropertyRange;

public class TPPropertyRangeHandler extends TriplePredicateHandler {

    public TPPropertyRangeHandler(OWLRDFConsumer consumer) {
        super(consumer, OWLRDFVocabulary.RDFS_RANGE.getIRI());
    }

    public boolean canHandleStreaming(String subject, String predicate, String object) {
        if (!consumer.isAnonymousNode(object)) {
            if (getConsumer().isObjectPropertyOnly(subject)) return true;
            else if (getConsumer().isDataPropertyOnly(subject)) return true;
        }
        return false;
    }
    public void handleTriple(String subject,String predicate,String object) {
        if (getConsumer().isObjectPropertyOnly(subject))
            translateObjectPropertyRange(subject, object, predicate);
        else if (getConsumer().isDataPropertyOnly(subject)) {
            addAxiom(DataPropertyRange.create(translateDataProperty(subject), translateDataRange(object)));
            consumeTriple(subject, predicate, object);
        } else if(getConsumer().isAnnotationProperty(subject)) {
                consumer.hasAnnotations=true;
                consumeTriple(subject, predicate, object);
        } else if (getConsumer().isDataRange(object)) {
            // Assume data property
            addAxiom(DataPropertyRange.create(translateDataProperty(subject), translateDataRange(object)));
            consumeTriple(subject, predicate, object);
        } else if (getConsumer().isClass(object)) {
            // Assume object property
            translateObjectPropertyRange(subject, object, predicate);
        } else throw new RuntimeException("Could not disambiguate predicate "+predicate+" and object "+object+". "); 
    }
    protected void translateObjectPropertyRange(String subject,String object,String predicate) {
        addAxiom(ObjectPropertyRange.create(translateObjectProperty(subject), translateClassExpression(object)));
        consumeTriple(subject, predicate, object);
    }
}
