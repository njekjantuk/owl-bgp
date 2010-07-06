package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.owlapi.model.IRI;

public interface TripleHandler {
    void handlePrefixDirective(String prefixName, String prefix);
    void handleBaseDirective(String base);
    void handleComment(String comment);
    void handleTriple(String subject, String predicate, String object);
    void handleTriple(IRI subject, IRI predicate, String object);
    void handleTriple(IRI subject, IRI predicate, String object, String lang);
    void handleTriple(IRI subject, IRI predicate, String object, IRI datatype);
    void handleEnd();
}
