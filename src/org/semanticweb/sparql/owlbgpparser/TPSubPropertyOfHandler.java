package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.SubDataPropertyOf;
import org.semanticweb.sparql.owlbgp.model.axioms.SubObjectPropertyOf;

public class TPSubPropertyOfHandler extends TriplePredicateHandler {

    public TPSubPropertyOfHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.RDFS_SUB_PROPERTY_OF.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        return false;
    }
    public void handleTriple(Identifier subject,Identifier predicate,Identifier object) {
        if (consumer.isObjectPropertyOnly(subject) || consumer.isObjectPropertyOnly(object))
            translateSubObjectProperty(subject, predicate, object);
        // If any one of the properties is a data property then assume both are
        else if (consumer.isDataPropertyOnly(subject) && consumer.isDataPropertyOnly(object))
            translateSubDataProperty(subject, predicate, object);
        else {
            // Check for range statements
            Identifier subPropRange=consumer.getResourceObject(subject, Vocabulary.RDFS_RANGE.getIRI(), false);
            if (subPropRange!=null) {
                if (consumer.isDataRange(subPropRange))
                    translateSubDataProperty(subject, predicate, object);
                else
                    translateSubObjectProperty(subject, predicate, object);
                return;
            }
            Identifier supPropRange = consumer.getResourceObject(subject, Vocabulary.RDFS_RANGE.getIRI(), false);
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
    protected void translateSubObjectProperty(Identifier subject,Identifier predicate,Identifier object) {
        addAxiom(SubObjectPropertyOf.create(translateObjectProperty(subject), translateObjectProperty(object)));
        consumeTriple(subject, predicate, object);
    }
    protected void translateSubDataProperty(Identifier subject,Identifier predicate,Identifier object) {
        addAxiom(SubDataPropertyOf.create(translateDataProperty(subject), translateDataProperty(object)));
        consumeTriple(subject, predicate, object);
    }
}
