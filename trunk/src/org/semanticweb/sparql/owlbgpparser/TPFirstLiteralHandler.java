package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.ILiteral;
import org.semanticweb.sparql.owlbgp.model.Identifier;

public class TPFirstLiteralHandler extends AbstractLiteralTripleHandler {

    public TPFirstLiteralHandler(OWLRDFConsumer consumer) {
        super(consumer);
    }

    public boolean canHandle(Identifier subject, Identifier predicate, ILiteral object) {
        return predicate.equals(Vocabulary.RDF_FIRST.getIRI());
    }
    public boolean canHandleStreaming(Identifier subject, Identifier predicate, ILiteral object) {
        return predicate.equals(Vocabulary.RDF_FIRST.getIRI());
    }
    public void handleTriple(Identifier subject, Identifier predicate, ILiteral object) {
        getConsumer().addFirst(subject, object);
        consumeTriple(subject, predicate, object);
    }
}
