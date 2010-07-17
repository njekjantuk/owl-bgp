package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Identifier;

public class TPRestHandler extends TriplePredicateHandler {

    protected  static int count = 0;

    public TPRestHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.RDF_REST.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        return true;
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        if (!object.equals(Vocabulary.RDF_NIL.getIRI())) 
            getConsumer().addRest(subject, object);
        else
            count++;
        consumeTriple(subject, predicate, object);
    }
}
