package org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.DisjointObjectProperties;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.TriplePredicateHandler;

public class TPDisjointObjectPropertiesHandler extends TriplePredicateHandler {

    public TPDisjointObjectPropertiesHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_DISJOINT_OBJECT_PROPERTIES.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        return true;
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        Set<ObjectPropertyExpression> properties = new HashSet<ObjectPropertyExpression>();
        properties.add(consumer.translateObjectPropertyExpression(subject));
        properties.add(consumer.translateObjectPropertyExpression(object));
        consumer.addAxiom(DisjointObjectProperties.create(properties));
        consumer.consumeTriple(subject, predicate, object);
    }
}
