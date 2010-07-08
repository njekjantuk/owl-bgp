package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.DataPropertyDomain;

public class TPDataPropertDomainHandler extends TriplePredicateHandler {

    public TPDataPropertDomainHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_DATA_PROPERTY_DOMAIN.getIRI());
    }

    public boolean canHandleStreaming(String subject, String predicate, String object) {
        return !consumer.isAnonymousNode(object);
    }
    public void handleTriple(String subject, String predicate, String object) {
        addAxiom(DataPropertyDomain.create(translateDataProperty(subject),translateClassExpression(object)));
        consumeTriple(subject, predicate, object);
    }
}
