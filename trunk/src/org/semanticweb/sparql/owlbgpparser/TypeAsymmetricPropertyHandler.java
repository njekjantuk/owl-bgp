package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.AsymmetricObjectProperty;

public class TypeAsymmetricPropertyHandler extends BuiltInTypeHandler {

    public TypeAsymmetricPropertyHandler(OWLRDFConsumer consumer) {
        super(consumer, OWLRDFVocabulary.OWL_ASYMMETRIC_PROPERTY.getIRI());
    }

    public boolean canHandleStreaming(String subject, String predicate, String object) {
        return !consumer.isAnonymousNode(subject);
    }
    public void handleTriple(String subject, String predicate, String object) {
        consumer.addOWLObjectProperty(subject);
        addAxiom(AsymmetricObjectProperty.create(translateObjectProperty(subject)));
        consumeTriple(subject, predicate, object);
    }

}
