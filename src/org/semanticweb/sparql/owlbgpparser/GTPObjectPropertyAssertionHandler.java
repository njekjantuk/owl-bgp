package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.ObjectPropertyAssertion;

public class GTPObjectPropertyAssertionHandler extends AbstractResourceTripleHandler {
    public GTPObjectPropertyAssertionHandler(OWLRDFConsumer consumer) {
        super(consumer);
    }
    public boolean canHandle(String subject, String predicate, String object) {
        return !consumer.isAnnotationProperty(subject) && !Vocabulary.BUILT_IN_VOCABULARY_IRIS.contains(predicate) && !getConsumer().isOntology(subject);
    }
    public boolean canHandleStreaming(String subject, String predicate, String object) {
        return false;
    }
    public void handleTriple(String subject, String predicate, String object) {
        consumeTriple(subject, predicate, object);
        addAxiom(ObjectPropertyAssertion.create(translateObjectProperty(predicate), translateIndividual(subject), translateIndividual(object)));
    }
}
