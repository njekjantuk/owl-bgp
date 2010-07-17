package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Identifier;

public class TPImportsHandler extends TriplePredicateHandler {

    public TPImportsHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_IMPORTS.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        return true;
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        consumeTriple(subject, predicate, object);
    }
}
