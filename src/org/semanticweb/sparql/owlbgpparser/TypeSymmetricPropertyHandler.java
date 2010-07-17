package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.SymmetricObjectProperty;

public class TypeSymmetricPropertyHandler extends BuiltInTypeHandler {

    public TypeSymmetricPropertyHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_SYMMETRIC_PROPERTY.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        return !consumer.isAnonymousNode(subject);
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        addAxiom(SymmetricObjectProperty.create(translateObjectProperty(subject)));
        consumeTriple(subject, predicate, object);
    }
}
