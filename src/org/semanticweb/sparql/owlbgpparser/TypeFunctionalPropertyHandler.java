package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.FunctionalDataProperty;
import org.semanticweb.sparql.owlbgp.model.FunctionalObjectProperty;
import org.semanticweb.sparql.owlbgp.model.Identifier;

public class TypeFunctionalPropertyHandler extends BuiltInTypeHandler {

    public TypeFunctionalPropertyHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_FUNCTIONAL_PROPERTY.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        return consumer.isObjectPropertyOnly(subject) || consumer.isDataPropertyOnly(subject);
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
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
