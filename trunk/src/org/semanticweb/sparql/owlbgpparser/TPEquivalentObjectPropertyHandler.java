package org.semanticweb.sparql.owlbgpparser;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.EquivalentObjectProperties;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;

public class TPEquivalentObjectPropertyHandler extends TriplePredicateHandler {

    public TPEquivalentObjectPropertyHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_EQUIVALENT_OBJECT_PROPERTIES.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        return true;
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        Set<ObjectPropertyExpression> properties=new HashSet<ObjectPropertyExpression>();
        properties.add(translateObjectProperty(subject));
        properties.add(translateObjectProperty(object));
        addAxiom(EquivalentObjectProperties.create(properties));
        consumeTriple(subject, predicate, object);
    }
}
