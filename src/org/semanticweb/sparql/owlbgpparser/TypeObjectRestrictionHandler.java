package org.semanticweb.sparql.owlbgpparser;

public class TypeObjectRestrictionHandler extends BuiltInTypeHandler {

    public TypeObjectRestrictionHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_OBJECT_RESTRICTION.getIRI());
    }

    public void handleTriple(String subject, String predicate, String object) {
        consumeTriple(subject, predicate, object);
        consumer.addRestriction(subject);
    }
}
