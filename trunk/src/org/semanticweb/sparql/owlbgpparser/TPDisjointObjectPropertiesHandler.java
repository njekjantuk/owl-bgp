package org.semanticweb.sparql.owlbgpparser;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.DisjointObjectProperties;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.ObjectPropertyExpression;

public class TPDisjointObjectPropertiesHandler extends TriplePredicateHandler {

    public TPDisjointObjectPropertiesHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_DISJOINT_OBJECT_PROPERTIES.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        return true;
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        Set<ObjectPropertyExpression> properties = new HashSet<ObjectPropertyExpression>();
        properties.add(translateObjectProperty(subject));
        properties.add(translateObjectProperty(object));
        addAxiom(DisjointObjectProperties.create(properties));
        consumeTriple(subject, predicate, object);
    }
}
