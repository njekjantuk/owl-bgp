package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.SymmetricObjectProperty;

public class TypeSymmetricPropertyHandler extends BuiltInTypeHandler {

    public TypeSymmetricPropertyHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_SYMMETRIC_PROPERTY.getIRI());
    }

    public boolean canHandleStreaming(String subject, String predicate, String object) {
        return !consumer.isAnonymousNode(subject);
    }

    public void handleTriple(String subject, String predicate, String object) {
        addAxiom(SymmetricObjectProperty.create(translateObjectProperty(subject)));
        consumeTriple(subject, predicate, object);
    }
}
