package org.semanticweb.sparql.owlbgpparser;

public class TypeAnnotationHandler extends BuiltInTypeHandler {

    public TypeAnnotationHandler(OWLRDFConsumer consumer) {
        super(consumer, OWLRDFVocabulary.OWL_ANNOTATION.getIRI());
    }
    
    public void handleTriple(String subject, String predicate, String object) {
        consumer.addAnnotationIRI(subject);
    }
}
