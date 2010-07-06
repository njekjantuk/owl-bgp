package org.semanticweb.sparql.owlbgpparser;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.DataPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.EquivalentDataProperties;
import org.semanticweb.sparql.owlbgp.model.EquivalentObjectProperties;
import org.semanticweb.sparql.owlbgp.model.ObjectPropertyExpression;

public class TPEquivalentPropertyHandler extends TriplePredicateHandler {

    public TPEquivalentPropertyHandler(OWLRDFConsumer consumer) {
        super(consumer, OWLRDFVocabulary.OWL_EQUIVALENT_PROPERTY.getIRI());
    }

    public boolean canHandleStreaming(String subject, String predicate, String object) {
        return (consumer.isObjectPropertyOnly(subject) && consumer.isObjectPropertyOnly(object)) 
            || (consumer.isDataPropertyOnly(subject) && consumer.isDataPropertyOnly(object));
    }
    public void handleTriple(String subject, String predicate, String object) {
        // If either is an object property then translate as object properties
        if (consumer.isObjectPropertyOnly(subject) || consumer.isObjectPropertyOnly(object))
            translateEquivalentObjectProperties(subject, predicate, object);
        else if (consumer.isDataPropertyOnly(subject) || consumer.isDataPropertyOnly(object)) {
            Set<DataPropertyExpression> props = new HashSet<DataPropertyExpression>();
            props.add(translateDataProperty(subject));
            props.add(translateDataProperty(object));
            addAxiom(EquivalentDataProperties.create(props));
            consumeTriple(subject, predicate, object);
        } else throw new RuntimeException("Cannot disambiguate properties "+subject+" and "+object+". ");
    }
    protected void translateEquivalentObjectProperties(String subject, String predicate, String object) {
        Set<ObjectPropertyExpression> props = new HashSet<ObjectPropertyExpression>();
        props.add(translateObjectProperty(subject));
        props.add(translateObjectProperty(object));
        addAxiom(EquivalentObjectProperties.create(props));
        consumeTriple(subject, predicate, object);
    }
}
