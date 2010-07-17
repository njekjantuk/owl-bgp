package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.InverseObjectProperties;

public class TPInverseOfHandler extends TriplePredicateHandler {

    public TPInverseOfHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_INVERSE_OF.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        consumer.addObjectProperty(subject);
        consumer.addObjectProperty(object);
        return !isSubjectOrObjectAnonymous(subject, object);
    }
    public boolean canHandle(Identifier subject, Identifier predicate, Identifier object) {
        return !isSubjectOrObjectAnonymous(subject, object);
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        addAxiom(InverseObjectProperties.create(translateObjectProperty(subject),translateObjectProperty(object)));
        consumeTriple(subject, predicate, object);
    }
}
