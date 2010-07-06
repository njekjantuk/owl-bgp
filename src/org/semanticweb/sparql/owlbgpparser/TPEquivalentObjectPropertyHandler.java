package org.semanticweb.sparql.owlbgpparser;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.EquivalentObjectProperties;
import org.semanticweb.sparql.owlbgp.model.ObjectPropertyExpression;

public class TPEquivalentObjectPropertyHandler extends TriplePredicateHandler {

    public TPEquivalentObjectPropertyHandler(OWLRDFConsumer consumer) {
        super(consumer, OWLRDFVocabulary.OWL_EQUIVALENT_OBJECT_PROPERTIES.getIRI());
    }

    public boolean canHandleStreaming(String subject, String predicate, String object) {
        return true;
    }
    public void handleTriple(String subject, String predicate, String object) {
        Set<ObjectPropertyExpression> properties=new HashSet<ObjectPropertyExpression>();
        properties.add(translateObjectProperty(subject));
        properties.add(translateObjectProperty(object));
        addAxiom(EquivalentObjectProperties.create(properties));
        consumeTriple(subject, predicate, object);
    }
}
