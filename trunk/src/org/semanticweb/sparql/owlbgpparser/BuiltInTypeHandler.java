package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.IRI;
import org.semanticweb.sparql.owlbgp.model.Identifier;

public abstract class BuiltInTypeHandler extends TriplePredicateHandler {

    protected final IRI typeIRI;

    public BuiltInTypeHandler(OWLRDFConsumer consumer, IRI typeIRI) {
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
