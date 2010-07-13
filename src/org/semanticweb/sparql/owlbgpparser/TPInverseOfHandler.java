package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.InverseObjectProperties;

public class TPInverseOfHandler extends TriplePredicateHandler {

    public TPInverseOfHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_INVERSE_OF.getIRI());
    }

    public boolean canHandleStreaming(String subject, String predicate, String object) {
        consumer.addObjectProperty(subject);
        consumer.addObjectProperty(object);
        return !isSubjectOrObjectAnonymous(subject, object);
    }
    public boolean canHandle(String subject, String predicate, String object) {
        return !isSubjectOrObjectAnonymous(subject, object);
    }
    public void handleTriple(String subject, String predicate, String object) {
        addAxiom(InverseObjectProperties.create(translateObjectProperty(subject),translateObjectProperty(object)));
        consumeTriple(subject, predicate, object);
    }
}
