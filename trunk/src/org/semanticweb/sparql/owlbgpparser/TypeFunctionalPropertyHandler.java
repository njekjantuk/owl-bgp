package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.FunctionalDataProperty;
import org.semanticweb.sparql.owlbgp.model.FunctionalObjectProperty;

public class TypeFunctionalPropertyHandler extends BuiltInTypeHandler {

    public TypeFunctionalPropertyHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_FUNCTIONAL_PROPERTY.getIRI());
    }

    public boolean canHandleStreaming(String subject, String predicate, String object) {
        return consumer.isObjectPropertyOnly(subject) || consumer.isDataPropertyOnly(subject);
    }
    public void handleTriple(String subject, String predicate, String object) {
        if (consumer.isObjectPropertyOnly(subject)) {
            addAxiom(FunctionalObjectProperty.create(translateObjectProperty(subject)));
            consumeTriple(subject, predicate, object);
        } else if (consumer.isDataPropertyOnly(subject)) {
            addAxiom(FunctionalDataProperty.create(translateDataProperty(subject)));
            consumeTriple(subject, predicate, object);
        } else {
            throw new IllegalArgumentException("Property "+subject+" cannot be identified as object or data property (variable). ");
        }
    }
}
