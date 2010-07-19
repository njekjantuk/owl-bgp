package org.semanticweb.sparql.owlbgpparser;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.EquivalentDataProperties;
import org.semanticweb.sparql.owlbgp.model.properties.DataPropertyExpression;

public class TPEquivalentDataPropertyHandler extends TriplePredicateHandler {

    public TPEquivalentDataPropertyHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_EQUIVALENT_DATA_PROPERTIES.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        return true;
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        Set<DataPropertyExpression> properties = new HashSet<DataPropertyExpression>();
        properties.add(translateDataProperty(subject));
        properties.add(translateDataProperty(object));
        addAxiom(EquivalentDataProperties.create(properties));
        consumeTriple(subject, predicate, object);
    }
}

