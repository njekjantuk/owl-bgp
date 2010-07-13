package org.semanticweb.sparql.owlbgpparser;

public class TypeClassHandler extends BuiltInTypeHandler {

    public TypeClassHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_CLASS.getIRI());
    }

    public void handleTriple(String subject, String predicate, String object) {
        consumer.addClass(subject);
    }
}
