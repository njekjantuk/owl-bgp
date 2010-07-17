package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Identifier;

public class TypeOnPropertyHandler extends BuiltInTypeHandler {

    public TypeOnPropertyHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_ON_PROPERTY.getIRI());
    }

    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        consumer.addOnProperty(subject);
        consumeTriple(subject, predicate, object);
    }
}
