package org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.EquivalentDataProperties;
import org.semanticweb.sparql.owlbgp.model.properties.DataPropertyExpression;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.TriplePredicateHandler;

public class TPEquivalentDataPropertyHandler extends TriplePredicateHandler {

    public TPEquivalentDataPropertyHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_EQUIVALENT_DATA_PROPERTIES.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        return true;
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        Set<DataPropertyExpression> properties = new HashSet<DataPropertyExpression>();
        properties.add(consumer.translateDataPropertyExpression(subject));
        properties.add(consumer.translateDataPropertyExpression(object));
        consumer.addAxiom(EquivalentDataProperties.create(properties));
        consumer.consumeTriple(subject, predicate, object);
    }
}

