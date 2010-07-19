package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.FunctionalObjectProperty;

public class TypeFunctionalObjectPropertyHandler extends BuiltInTypeHandler {

    public TypeFunctionalObjectPropertyHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_FUNCTIONAL_OBJECT_PROPERTY.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        return !consumer.isAnonymousNode(subject);
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        addAxiom(FunctionalObjectProperty.create(translateObjectProperty(subject)));
        consumeTriple(subject, predicate, object);
    }
}
