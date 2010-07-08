package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.FunctionalObjectProperty;

public class TypeFunctionalObjectPropertyHandler extends BuiltInTypeHandler {

    public TypeFunctionalObjectPropertyHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_FUNCTIONAL_PROPERTY.getIRI());
    }

    public boolean canHandleStreaming(String subject, String predicate, String object) {
        return !consumer.isAnonymousNode(subject);
    }
    public void handleTriple(String subject, String predicate, String object) {
        consumer.addOWLObjectProperty(subject);
        addAxiom(FunctionalObjectProperty.create(translateObjectProperty(subject)));
        consumeTriple(subject, predicate, object);
    }
}
