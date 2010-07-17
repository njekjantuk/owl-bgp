package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.DataPropertyDomain;
import org.semanticweb.sparql.owlbgp.model.Identifier;

public class TPDataPropertDomainHandler extends TriplePredicateHandler {

    public TPDataPropertDomainHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_DATA_PROPERTY_DOMAIN.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        return !consumer.isAnonymousNode(object);
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        addAxiom(DataPropertyDomain.create(translateDataProperty(subject),translateClassExpression(object)));
        consumeTriple(subject, predicate, object);
    }
}
