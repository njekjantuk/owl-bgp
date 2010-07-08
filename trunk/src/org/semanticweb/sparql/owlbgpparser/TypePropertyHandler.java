package org.semanticweb.sparql.owlbgpparser;

public class TypePropertyHandler extends BuiltInTypeHandler {

    public TypePropertyHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.RDF_PROPERTY.getIRI());
    }

    public void handleTriple(String subject, String predicate, String object) {
        consumeTriple(subject, predicate, object);
    }
}
