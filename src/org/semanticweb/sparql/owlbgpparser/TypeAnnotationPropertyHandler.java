package org.semanticweb.sparql.owlbgpparser;

public class TypeAnnotationPropertyHandler extends BuiltInTypeHandler{

    public TypeAnnotationPropertyHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_ANNOTATION_PROPERTY.getIRI());
    }

    public void handleTriple(String subject, String predicate, String object) {
        consumer.addAnnotationProperty(subject);
        consumeTriple(subject, predicate, object);
    }
}
