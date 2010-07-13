package org.semanticweb.sparql.owlbgpparser;

public class TypeOnPropertyHandler extends BuiltInTypeHandler {

    public TypeOnPropertyHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_ON_PROPERTY.getIRI());
    }

    public void handleTriple(String subject, String predicate, String object) {
        consumer.addOnProperty(subject);
        consumeTriple(subject, predicate, object);
    }
}
