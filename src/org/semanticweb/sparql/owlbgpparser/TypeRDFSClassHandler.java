package org.semanticweb.sparql.owlbgpparser;

public class TypeRDFSClassHandler extends BuiltInTypeHandler {

    public TypeRDFSClassHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.RDFS_CLASS.getIRI());
    }

    public void handleTriple(String subject, String predicate, String object) {
        consumer.addOWLClass(subject);
        consumeTriple(subject, predicate, object);
    }
}
