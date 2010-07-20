package org.semanticweb.sparql.owlbgpparser.triplehandlers.builtinpredicate;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.DisjointDataProperties;
import org.semanticweb.sparql.owlbgp.model.properties.DataPropertyExpression;
import org.semanticweb.sparql.owlbgpparser.TripleConsumer;
import org.semanticweb.sparql.owlbgpparser.Vocabulary;
import org.semanticweb.sparql.owlbgpparser.triplehandlers.TriplePredicateHandler;

public class TPDisjointDataPropertiesHandler extends TriplePredicateHandler {

    public TPDisjointDataPropertiesHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_DISJOINT_DATA_PROPERTIES.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        // We can always handle disjoint data properties in a streaming
        // manner, because they are either named, or inverses of properties.
        return true;
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        Set<DataPropertyExpression> properties = new HashSet<DataPropertyExpression>();
        properties.add(consumer.translateDataPropertyExpression(subject));
        properties.add(consumer.translateDataPropertyExpression(object));
        consumer.addAxiom(DisjointDataProperties.create(properties));
        consumer.consumeTriple(subject, predicate, object);
    }
}
