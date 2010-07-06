package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.owlapi.model.IRI;

public class ConsoleTripleHandler implements TripleHandler {
    public void handleTriple(String subject, String predicate, String object) {
        System.out.println(subject + " --> " + predicate + " --> " + object);
    }
    public void handleTriple(IRI subject, IRI predicate, String object) {
        System.out.println(subject + " --> " + predicate + " --> " + object);
    }
    public void handleTriple(IRI subject, IRI predicate, String object, String lang) {
        System.out.println(subject + " --> " + predicate + " --> " + object + "@" + lang);
    }
    public void handleTriple(IRI subject, IRI predicate, String object, IRI datatype) {
        System.out.println(subject + " --> " + predicate + " --> " + object + "^^" + datatype);
    }
    public void handlePrefixDirective(String prefixName, String prefix) {
        System.out.println("PREFIX: " + prefixName + " -> " + prefix);
    }
    public void handleBaseDirective(String base) {
        System.out.println("BASE: " + base);
    }
    public void handleComment(String comment) {
        System.out.println("COMMENT: " + comment);
    }
    public void handleEnd() {
        System.out.println("END");
    }
}
