package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Identifier;

public class TPFirstResourceHandler extends TriplePredicateHandler {

    public TPFirstResourceHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.RDF_FIRST.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        return true;
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        consumer.addFirst(subject, object);
        consumeTriple(subject, predicate, object);
    }
}
