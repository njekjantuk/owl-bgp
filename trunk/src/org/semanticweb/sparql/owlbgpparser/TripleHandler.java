package org.semanticweb.sparql.owlbgpparser;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Axiom;
import org.semanticweb.sparql.owlbgp.model.Datatype;

public interface TripleHandler {
    void handlePrefixDirective(String prefixName, String prefix);
    void handleBaseDirective(String base);
    void handleComment(String comment);
    void handleTriple(String subject, String predicate, String object);
    void handleLiteralTriple(String subject, String predicate, String literal,String langTag, Datatype datatype);
    void handleLiteralTriple(String subject, String predicate, String literal,Datatype datatype);
    void handleEnd();
    public Set<Axiom> getParsedAxioms();
}
