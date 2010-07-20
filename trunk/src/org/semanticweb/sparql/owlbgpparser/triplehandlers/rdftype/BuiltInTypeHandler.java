package org.semanticweb.sparql.owlbgpparser.triplehandlers.rdftype;

import org.semanticweb.sparql.owlbgp.model.IRI;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgpparser.TripleConsumer;
import org.semanticweb.sparql.owlbgpparser.Vocabulary;
import org.semanticweb.sparql.owlbgpparser.triplehandlers.TriplePredicateHandler;

public abstract class BuiltInTypeHandler extends TriplePredicateHandler {

    protected final IRI typeIRI;

    public BuiltInTypeHandler(TripleConsumer consumer, IRI typeIRI) {
        super(consumer, Vocabulary.RDF_TYPE.getIRI());
        this.typeIRI = typeIRI;
    }
    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        return true;
    }
    public boolean canHandle(Identifier subject, Identifier predicate, Identifier object) {
        return predicate.equals(Vocabulary.RDF_TYPE.getIRI()) && object.equals(typeIRI);
    }
    public IRI getTypeIRI() {
        return typeIRI;
    }
}
