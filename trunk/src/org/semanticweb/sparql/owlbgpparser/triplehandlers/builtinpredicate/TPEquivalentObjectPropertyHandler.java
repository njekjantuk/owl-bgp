package org.semanticweb.sparql.owlbgpparser.triplehandlers.builtinpredicate;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.EquivalentObjectProperties;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;
import org.semanticweb.sparql.owlbgpparser.TripleConsumer;
import org.semanticweb.sparql.owlbgpparser.Vocabulary;
import org.semanticweb.sparql.owlbgpparser.triplehandlers.TriplePredicateHandler;

public class TPEquivalentObjectPropertyHandler extends TriplePredicateHandler {

    public TPEquivalentObjectPropertyHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_EQUIVALENT_OBJECT_PROPERTIES.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        return true;
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        Set<ObjectPropertyExpression> properties=new HashSet<ObjectPropertyExpression>();
        properties.add(consumer.translateObjectPropertyExpression(subject));
        properties.add(consumer.translateObjectPropertyExpression(object));
        consumer.addAxiom(EquivalentObjectProperties.create(properties));
        consumer.consumeTriple(subject, predicate, object);
    }
}
