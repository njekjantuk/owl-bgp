package org.semanticweb.sparql.owlbgpparser;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.DisjointObjectProperties;
import org.semanticweb.sparql.owlbgp.model.ObjectPropertyExpression;

public class TPDisjointObjectPropertiesHandler extends TriplePredicateHandler {

    public TPDisjointObjectPropertiesHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_DISJOINT_OBJECT_PROPERTIES.getIRI());
    }

    public boolean canHandleStreaming(String subject, String predicate, String object) {
        return true;
    }
    public void handleTriple(String subject, String predicate, String object) {
        Set<ObjectPropertyExpression> properties = new HashSet<ObjectPropertyExpression>();
        properties.add(translateObjectProperty(subject));
        properties.add(translateObjectProperty(object));
        addAxiom(DisjointObjectProperties.create(properties));
        consumeTriple(subject, predicate, object);
    }
}
