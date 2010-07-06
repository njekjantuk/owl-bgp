package org.semanticweb.sparql.owlbgpparser;

public class TypeAllDifferentHandler extends BuiltInTypeHandler {

    public TypeAllDifferentHandler(OWLRDFConsumer consumer) {
        super(consumer, OWLRDFVocabulary.OWL_ALL_DIFFERENT.getIRI());
    }
    public void handleTriple(String subject, String predicate, String object) {
        consumeTriple(subject, predicate, object);
    }
}
