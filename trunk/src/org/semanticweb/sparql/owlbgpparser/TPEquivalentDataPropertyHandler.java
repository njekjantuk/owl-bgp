package org.semanticweb.sparql.owlbgpparser;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.DataPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.EquivalentDataProperties;

public class TPEquivalentDataPropertyHandler extends TriplePredicateHandler {

    public TPEquivalentDataPropertyHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_EQUIVALENT_DATA_PROPERTIES.getIRI());
    }

    public boolean canHandleStreaming(String subject, String predicate, String object) {
        return true;
    }
    public void handleTriple(String subject, String predicate, String object) {
        Set<DataPropertyExpression> properties = new HashSet<DataPropertyExpression>();
        properties.add(translateDataProperty(subject));
        properties.add(translateDataProperty(object));
        addAxiom(EquivalentDataProperties.create(properties));
        consumeTriple(subject, predicate, object);
    }
}

