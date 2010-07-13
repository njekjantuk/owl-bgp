package org.semanticweb.sparql.owlbgpparser;

public class TypeDatatypeHandler extends BuiltInTypeHandler {

    public TypeDatatypeHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.RDFS_DATATYPE.getIRI());
    }

    public void handleTriple(String subject, String predicate, String object) {
        consumer.addDataRange(subject);
    }
}
