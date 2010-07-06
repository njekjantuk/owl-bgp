package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.DisjointDataProperties;
import org.semanticweb.sparql.owlbgp.model.DisjointObjectProperties;

public class TPPropertyDisjointWithHandler extends TriplePredicateHandler {

    public TPPropertyDisjointWithHandler(OWLRDFConsumer consumer) {
        super(consumer, OWLRDFVocabulary.OWL_PROPERTY_DISJOINT_WITH.getIRI());
    }

    public boolean canHandleStreaming(String subject, String predicate, String object) {
        return (consumer.isObjectPropertyOnly(subject) && consumer.isObjectPropertyOnly(object)) 
            || (consumer.isDataPropertyOnly(subject) && consumer.isDataPropertyOnly(object));
    }
    public void handleTriple(String subject, String predicate, String object) {
        if (consumer.isDataPropertyOnly(subject) || consumer.isDataPropertyOnly(object)) {
            addAxiom(DisjointDataProperties.create(translateDataProperty(subject), translateDataProperty(object)));
            consumeTriple(subject, predicate, object);
        } else if (consumer.isObjectPropertyOnly(subject) || consumer.isObjectPropertyOnly(object)) {
            addAxiom(DisjointObjectProperties.create(translateObjectProperty(subject), translateObjectProperty(object)));
            consumeTriple(subject, predicate, object);
        }
    }
}
