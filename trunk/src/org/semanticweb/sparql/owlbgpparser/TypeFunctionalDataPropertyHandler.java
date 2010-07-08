package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.FunctionalDataProperty;

public class TypeFunctionalDataPropertyHandler extends BuiltInTypeHandler {

    public TypeFunctionalDataPropertyHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_FUNCTIONAL_DATA_PROPERTY.getIRI());
    }

    public void handleTriple(String subject, String predicate, String object) {
        getConsumer().addOWLDataProperty(subject);
        addAxiom(FunctionalDataProperty.create(translateDataProperty(subject)));
        consumeTriple(subject, predicate, object);
    }
}
