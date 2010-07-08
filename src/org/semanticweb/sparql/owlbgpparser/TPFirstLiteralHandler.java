package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.ILiteral;

public class TPFirstLiteralHandler extends AbstractLiteralTripleHandler {

    public TPFirstLiteralHandler(OWLRDFConsumer consumer) {
        super(consumer);
    }

    public boolean canHandle(String subject, String predicate, ILiteral object) {
        return predicate.equals(Vocabulary.RDF_FIRST.getIRI());
    }
    public boolean canHandleStreaming(String subject, String predicate, ILiteral object) {
        return predicate.equals(Vocabulary.RDF_FIRST.getIRI());
    }
    public void handleTriple(String subject, String predicate, ILiteral object) {
        getConsumer().addFirst(subject, object);
        consumeTriple(subject, predicate, object);
    }
}
