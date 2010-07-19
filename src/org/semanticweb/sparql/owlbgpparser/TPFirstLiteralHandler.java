package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.literals.Literal;

public class TPFirstLiteralHandler extends AbstractLiteralTripleHandler {

    public TPFirstLiteralHandler(OWLRDFConsumer consumer) {
        super(consumer);
    }

    public boolean canHandle(Identifier subject, Identifier predicate, Literal object) {
        return predicate.equals(Vocabulary.RDF_FIRST.getIRI());
    }
    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Literal object) {
        return predicate.equals(Vocabulary.RDF_FIRST.getIRI());
    }
    public void handleTriple(Identifier subject, Identifier predicate, Literal object) {
        getConsumer().addFirst(subject, object);
        consumeTriple(subject, predicate, object);
    }
}
