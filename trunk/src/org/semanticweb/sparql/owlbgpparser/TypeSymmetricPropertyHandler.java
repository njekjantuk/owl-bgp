package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.SymmetricObjectProperty;

public class TypeSymmetricPropertyHandler extends BuiltInTypeHandler {

    public TypeSymmetricPropertyHandler(OWLRDFConsumer consumer) {
        super(consumer, OWLRDFVocabulary.OWL_SYMMETRIC_PROPERTY.getIRI());
    }

    public boolean canHandleStreaming(String subject, String predicate, String object) {
        consumer.addOWLObjectProperty(subject);
        return !consumer.isAnonymousNode(subject);
    }

    public void handleTriple(String subject, String predicate, String object) {
        consumer.addOWLObjectProperty(subject);
        addAxiom(SymmetricObjectProperty.create(translateObjectProperty(subject)));
        consumeTriple(subject, predicate, object);
    }
}
