package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.ILiteral;
import org.semanticweb.sparql.owlbgp.model.Identifier;

public abstract class AbstractLiteralTripleHandler extends AbstractTripleHandler {

    public AbstractLiteralTripleHandler(OWLRDFConsumer consumer) {
        super(consumer);
    }
    public abstract void handleTriple(Identifier subject, Identifier predicate, ILiteral object);

    public abstract boolean canHandle(Identifier subject, Identifier predicate, ILiteral object);

    public abstract boolean canHandleStreaming(Identifier subject, Identifier predicate, ILiteral object);
}
