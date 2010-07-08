package org.semanticweb.sparql.owlbgpparser;

public class TypeNamedIndividualHandler extends BuiltInTypeHandler {

    public TypeNamedIndividualHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_NAMED_INDIVIDUAL.getIRI());
    }

    public void handleTriple(String subject, String predicate, String object) {
        consumer.addIndividual(subject);
    }
}
