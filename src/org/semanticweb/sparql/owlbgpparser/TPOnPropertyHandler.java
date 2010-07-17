package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Identifier;

public class TPOnPropertyHandler extends TriplePredicateHandler {

    public TPOnPropertyHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_ON_PROPERTY.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        consumer.addRestriction(subject);
        return false;   
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
    }
}
