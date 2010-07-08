package org.semanticweb.sparql.owlbgpparser;

public class TypeDataRangeHandler extends BuiltInTypeHandler {

    public TypeDataRangeHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_DATA_RANGE.getIRI());
    }
    
    public void handleTriple(String subject, String predicate, String object) {
        if (!consumer.isAnonymousNode(subject)) {
            getConsumer().addOWLDataRange(subject);
            consumeTriple(subject, predicate, object);
        }
    }
}
