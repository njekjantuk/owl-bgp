package org.semanticweb.sparql.owlbgpparser;


import org.semanticweb.sparql.owlbgp.model.Datatype;
import org.xml.sax.SAXException;

public class OWLRDFConsumerAdapter extends OWLRDFConsumer implements TripleHandler {

    public OWLRDFConsumerAdapter() {
        super();
    }
    public void handlePrefixDirective(String prefixName, String prefix) {}
    public void handleBaseDirective(String base) {
        xmlBase=base;
    }
    public void handleComment(String comment) {}
    public void handleTriple(String subject, String predicate, String object) {
        try {
            statementWithResourceValue(subject, predicate, object);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }
    public void handleLiteralTriple(String subject, String predicate, String literal, String langTag, Datatype datatype) {
        try {
            statementWithLiteralValue(subject, predicate, literal, langTag, datatype);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }
    public void handleLiteralTriple(String subject, String predicate, String literal, Datatype datatype) {
        try {
            statementWithLiteralValue(subject, predicate, literal, "", datatype);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }
    public void handleEnd() {
        try {
            endModel();
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }
}


