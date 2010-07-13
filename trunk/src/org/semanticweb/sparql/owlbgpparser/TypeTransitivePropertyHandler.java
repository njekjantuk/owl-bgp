package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.TransitiveObjectProperty;

public class TypeTransitivePropertyHandler extends BuiltInTypeHandler {

    public TypeTransitivePropertyHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_TRANSITIVE_PROPERTY.getIRI());
    }

    public boolean canHandleStreaming(String subject, String predicate, String object) {
        return !consumer.isAnonymousNode(subject);
    }
    public void handleTriple(String subject, String predicate, String object) {
        addAxiom(TransitiveObjectProperty.create(translateObjectProperty(subject)));
        consumeTriple(subject, predicate, object);
    }
}
