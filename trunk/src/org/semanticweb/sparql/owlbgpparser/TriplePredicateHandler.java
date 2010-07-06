package org.semanticweb.sparql.owlbgpparser;

public abstract class TriplePredicateHandler extends AbstractResourceTripleHandler {
    
    protected final String predicateIRI;
    
    public TriplePredicateHandler(OWLRDFConsumer consumer, String predicateIRI) {
        super(consumer);
        this.predicateIRI=predicateIRI;
    }
    public boolean canHandle(String subject, String predicate, String object) {
        return predicate.equals(predicateIRI);
    }
    public String getPredicateIRI() {
        return predicateIRI;
    }
}
