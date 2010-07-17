package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.AsymmetricObjectProperty;
import org.semanticweb.sparql.owlbgp.model.Identifier;

public class TypeAsymmetricPropertyHandler extends BuiltInTypeHandler {

    public TypeAsymmetricPropertyHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_ASYMMETRIC_PROPERTY.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        return !consumer.isAnonymousNode(subject);
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        addAxiom(AsymmetricObjectProperty.create(translateObjectProperty(subject)));
        consumeTriple(subject, predicate, object);
    }

}
