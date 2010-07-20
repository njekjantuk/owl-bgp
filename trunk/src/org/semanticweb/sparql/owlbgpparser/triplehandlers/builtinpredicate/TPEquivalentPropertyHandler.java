package org.semanticweb.sparql.owlbgpparser.triplehandlers.builtinpredicate;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.EquivalentDataProperties;
import org.semanticweb.sparql.owlbgp.model.axioms.EquivalentObjectProperties;
import org.semanticweb.sparql.owlbgp.model.properties.DataPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;
import org.semanticweb.sparql.owlbgpparser.TripleConsumer;
import org.semanticweb.sparql.owlbgpparser.Vocabulary;
import org.semanticweb.sparql.owlbgpparser.triplehandlers.TriplePredicateHandler;

public class TPEquivalentPropertyHandler extends TriplePredicateHandler {

    public TPEquivalentPropertyHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_EQUIVALENT_PROPERTY.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        return (consumer.isObjectPropertyOnly(subject) && consumer.isObjectPropertyOnly(object)) 
            || (consumer.isDataPropertyOnly(subject) && consumer.isDataPropertyOnly(object));
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        // If either is an object property then translate as object properties
        if (consumer.isObjectPropertyOnly(subject) || consumer.isObjectPropertyOnly(object))
            translateEquivalentObjectProperties(subject, predicate, object);
        else if (consumer.isDataPropertyOnly(subject) || consumer.isDataPropertyOnly(object)) {
            Set<DataPropertyExpression> props = new HashSet<DataPropertyExpression>();
            props.add(consumer.translateDataPropertyExpression(subject));
            props.add(consumer.translateDataPropertyExpression(object));
            consumer.addAxiom(EquivalentDataProperties.create(props));
            consumer.consumeTriple(subject, predicate, object);
        } else throw new RuntimeException("Cannot disambiguate properties "+subject+" and "+object+". ");
    }
    protected void translateEquivalentObjectProperties(Identifier subject, Identifier predicate, Identifier object) {
        Set<ObjectPropertyExpression> props = new HashSet<ObjectPropertyExpression>();
        props.add(consumer.translateObjectPropertyExpression(subject));
        props.add(consumer.translateObjectPropertyExpression(object));
        consumer.addAxiom(EquivalentObjectProperties.create(props));
        consumer.consumeTriple(subject, predicate, object);
    }
}
