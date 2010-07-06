package org.semanticweb.sparql.owlbgpparser;

public class TypeListHandler extends BuiltInTypeHandler {

    public TypeListHandler(OWLRDFConsumer consumer) {
        super(consumer, OWLRDFVocabulary.RDF_LIST.getIRI());
    }

    public void handleTriple(String subject, String predicate, String object) {
        consumeTriple(subject, predicate, object);
        consumer.addList(subject);
    }
}
