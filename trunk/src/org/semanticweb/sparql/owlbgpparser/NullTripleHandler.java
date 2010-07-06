package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.owlapi.model.IRI;

public class NullTripleHandler implements TripleHandler {
    public void handlePrefixDirective(String prefixName, String prefix) {}
    public void handleBaseDirective(String base) {}
    public void handleComment(String comment) {}
    public void handleTriple(String subject, String predicate, String object) {}
    public void handleTriple(IRI subject, IRI predicate, String object) {}
    public void handleTriple(IRI subject, IRI predicate, String object, String lang) {}
    public void handleTriple(IRI subject, IRI predicate, String object, IRI datatype) {}
    public void handleEnd() {}
}
