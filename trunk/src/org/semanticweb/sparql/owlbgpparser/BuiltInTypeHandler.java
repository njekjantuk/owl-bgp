package org.semanticweb.sparql.owlbgpparser;

public abstract class BuiltInTypeHandler extends TriplePredicateHandler {

    protected final String typeIRI;

    public BuiltInTypeHandler(OWLRDFConsumer consumer, String typeIRI) {
        super(consumer, OWLRDFVocabulary.RDF_TYPE.getIRI());
        this.typeIRI = typeIRI;
    }
    public boolean canHandleStreaming(String subject, String predicate, String object) {
        return true;
    }
    public boolean canHandle(String subject, String predicate, String object) {
        return predicate.equals(OWLRDFVocabulary.RDF_TYPE.getIRI()) && object.equals(typeIRI);
    }
    public String getTypeIRI() {
        return typeIRI;
    }
}
