package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.ReflexiveObjectProperty;

public class TypeReflexivePropertyHandler extends BuiltInTypeHandler {

    public TypeReflexivePropertyHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_REFLEXIVE_PROPERTY.getIRI());
    }

    public boolean canHandleStreaming(String subject, String predicate, String object) {
        consumer.addOWLObjectProperty(subject);
        return !consumer.isAnonymousNode(subject);
    }
    public void handleTriple(String subject, String predicate, String object) {
        consumer.addOWLObjectProperty(subject);
        addAxiom(ReflexiveObjectProperty.create(translateObjectProperty(subject)));
        consumeTriple(subject, predicate, object);
    }
}
