package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.SubDataPropertyOf;
import org.semanticweb.sparql.owlbgp.model.SubObjectPropertyOf;

public class TPSubPropertyOfHandler extends TriplePredicateHandler {

    public TPSubPropertyOfHandler(OWLRDFConsumer consumer) {
        super(consumer, OWLRDFVocabulary.RDFS_SUB_PROPERTY_OF.getIRI());
    }

    public boolean canHandleStreaming(String subject, String predicate, String object) {
        return false;
    }
    public void handleTriple(String subject,String predicate,String object) {
        if (consumer.isObjectPropertyOnly(subject) || consumer.isObjectPropertyOnly(object))
            translateSubObjectProperty(subject, predicate, object);
        // If any one of the properties is a data property then assume both are
        else if (consumer.isDataPropertyOnly(subject) && consumer.isDataPropertyOnly(object))
            translateSubDataProperty(subject, predicate, object);
        else {
            // Check for range statements
            String subPropRange=consumer.getResourceObject(subject, OWLRDFVocabulary.RDFS_RANGE.getIRI(), false);
            if (subPropRange!=null) {
                if (consumer.isDataRange(subPropRange))
                    translateSubDataProperty(subject, predicate, object);
                else
                    translateSubObjectProperty(subject, predicate, object);
                return;
            }
            String supPropRange = consumer.getResourceObject(subject, OWLRDFVocabulary.RDFS_RANGE.getIRI(), false);
            if (supPropRange!=null) {
                if (consumer.isDataRange(supPropRange))
                    translateSubDataProperty(subject, predicate, object);
                else
                    translateSubObjectProperty(subject, predicate, object);
                return;
            }
            throw new RuntimeException("Cound not disambiguate properties "+subject+" and "+object+". ");
        }
    }
    protected void translateSubObjectProperty(String subject,String predicate,String object) {
        addAxiom(SubObjectPropertyOf.create(translateObjectProperty(subject), translateObjectProperty(object)));
        consumeTriple(subject, predicate, object);
    }
    protected void translateSubDataProperty(String subject,String predicate,String object) {
        addAxiom(SubDataPropertyOf.create(translateDataProperty(subject), translateDataProperty(object)));
        consumeTriple(subject, predicate, object);
    }
}
