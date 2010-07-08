package org.semanticweb.sparql.owlbgpparser;

public class TypeRDFPropertyHandler extends BuiltInTypeHandler {

    public TypeRDFPropertyHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_ON_PROPERTY.getIRI());
    }

    public void handleTriple(String subject, String predicate, String object) {
        consumeTriple(subject, predicate, object);
        consumer.addRDFProperty(subject);
    }
}
