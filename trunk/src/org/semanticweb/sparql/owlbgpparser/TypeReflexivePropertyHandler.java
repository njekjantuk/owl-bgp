package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.ReflexiveObjectProperty;

public class TypeReflexivePropertyHandler extends BuiltInTypeHandler {

    public TypeReflexivePropertyHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_REFLEXIVE_PROPERTY.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        return !consumer.isAnonymousNode(subject);
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        addAxiom(ReflexiveObjectProperty.create(translateObjectProperty(subject)));
        consumeTriple(subject, predicate, object);
    }
}
