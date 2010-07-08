package org.semanticweb.sparql.owlbgpparser;

public class TypeDataRestrictionHandler extends BuiltInTypeHandler {

    public TypeDataRestrictionHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_DATA_RESTRICTION.getIRI());
    }

    public void handleTriple(String subject, String predicate, String object) {
        consumeTriple(subject, predicate, object);
        consumer.addRestriction(subject);
    }
}
