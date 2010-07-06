package org.semanticweb.sparql.owlbgpparser;

public class TypeRestrictionHandler extends BuiltInTypeHandler {

    public TypeRestrictionHandler(OWLRDFConsumer consumer) {
        super(consumer, OWLRDFVocabulary.OWL_RESTRICTION.getIRI());
    }

    public void handleTriple(String subject, String predicate, String object) {
        consumeTriple(subject, predicate, object);
        consumer.addRestriction(subject);
        consumer.addOWLClass(subject);
    }
}
