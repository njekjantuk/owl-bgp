package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.dataranges.Datatype;
import org.xml.sax.SAXException;

public class OWLRDFConsumerAdapter extends OWLRDFConsumer implements TripleHandler {

    public OWLRDFConsumerAdapter() {
        super();
    }
    public void handlePrefixDirective(String prefixName, Identifier prefix) {}
    public void handleBaseDirective(Identifier base) {
        xmlBase=base;
    }
    public void handleComment(String comment) {}
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        try {
            statementWithResourceValue(subject, predicate, object);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }
    public void handleLiteralTriple(Identifier subject, Identifier predicate, String lexicalForm, String langTag, Datatype datatype) {
        try {
            statementWithLiteralValue(subject, predicate, lexicalForm, langTag, datatype);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }
    public void handleLiteralTriple(Identifier subject, Identifier predicate, String lexicalForm, Datatype datatype) {
        try {
            statementWithLiteralValue(subject, predicate, lexicalForm, "", datatype);
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


