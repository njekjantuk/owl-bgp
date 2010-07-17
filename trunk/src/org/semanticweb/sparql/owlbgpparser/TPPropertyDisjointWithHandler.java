package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.DisjointDataProperties;
import org.semanticweb.sparql.owlbgp.model.DisjointObjectProperties;
import org.semanticweb.sparql.owlbgp.model.Identifier;

public class TPPropertyDisjointWithHandler extends TriplePredicateHandler {

    public TPPropertyDisjointWithHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_PROPERTY_DISJOINT_WITH.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        return (consumer.isObjectPropertyOnly(subject) && consumer.isObjectPropertyOnly(object)) 
            || (consumer.isDataPropertyOnly(subject) && consumer.isDataPropertyOnly(object));
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        if (consumer.isDataPropertyOnly(subject) || consumer.isDataPropertyOnly(object)) {
            addAxiom(DisjointDataProperties.create(translateDataProperty(subject), translateDataProperty(object)));
            consumeTriple(subject, predicate, object);
        } else if (consumer.isObjectPropertyOnly(subject) || consumer.isObjectPropertyOnly(object)) {
            addAxiom(DisjointObjectProperties.create(translateObjectProperty(subject), translateObjectProperty(object)));
            consumeTriple(subject, predicate, object);
        }
    }
}
