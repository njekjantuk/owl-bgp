package org.semanticweb.sparql.owlbgpparser.triplehandlers.builtinpredicate;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.DisjointDataProperties;
import org.semanticweb.sparql.owlbgp.model.axioms.DisjointObjectProperties;
import org.semanticweb.sparql.owlbgpparser.TripleConsumer;
import org.semanticweb.sparql.owlbgpparser.Vocabulary;
import org.semanticweb.sparql.owlbgpparser.triplehandlers.TriplePredicateHandler;

public class TPPropertyDisjointWithHandler extends TriplePredicateHandler {

    public TPPropertyDisjointWithHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_PROPERTY_DISJOINT_WITH.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        return (consumer.isObjectPropertyOnly(subject) && consumer.isObjectPropertyOnly(object)) 
            || (consumer.isDataPropertyOnly(subject) && consumer.isDataPropertyOnly(object));
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        if (consumer.isDataPropertyOnly(subject) || consumer.isDataPropertyOnly(object)) {
            consumer.addAxiom(DisjointDataProperties.create(consumer.translateDataPropertyExpression(subject),consumer.translateDataPropertyExpression(object)));
            consumer.consumeTriple(subject, predicate, object);
        } else if (consumer.isObjectPropertyOnly(subject) || consumer.isObjectPropertyOnly(object)) {
            consumer.addAxiom(DisjointObjectProperties.create(consumer.translateObjectPropertyExpression(subject),consumer.translateObjectPropertyExpression(object)));
            consumer.consumeTriple(subject, predicate, object);
        }
    }
}
