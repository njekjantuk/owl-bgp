package org.semanticweb.sparql.owlbgpparser;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.DataPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.DisjointDataProperties;

public class TPDisjointDataPropertiesHandler extends TriplePredicateHandler {

    public TPDisjointDataPropertiesHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_DISJOINT_DATA_PROPERTIES.getIRI());
    }

    public boolean canHandleStreaming(String subject, String predicate, String object) {
        // We can always handle disjoint data properties in a streaming
        // manner, because they are either named, or inverses of properties.
        return true;
    }
    public void handleTriple(String subject, String predicate, String object) {
        Set<DataPropertyExpression> properties = new HashSet<DataPropertyExpression>();
        properties.add(translateDataProperty(subject));
        properties.add(translateDataProperty(object));
        addAxiom(DisjointDataProperties.create(properties));
        consumeTriple(subject, predicate, object);
    }
}
