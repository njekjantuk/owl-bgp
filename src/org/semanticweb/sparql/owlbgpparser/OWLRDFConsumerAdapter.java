package org.semanticweb.sparql.owlbgpparser;


import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.xml.sax.SAXException;

public class OWLRDFConsumerAdapter extends OWLRDFConsumer implements TripleHandler {

    public OWLRDFConsumerAdapter(OWLOntology ontology) {
        super(ontology);
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
    public void handleTriple(IRI subject, IRI predicate, String object) {
        try {
        	statementWithLiteralValue(subject.toString(), predicate.toString(), object, null, null);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }
    public void handleTriple(IRI subject, IRI predicate, String object, String lang) {
        try {
            statementWithLiteralValue(subject.toString(), predicate.toString(), object, lang, null);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }
    public void handleTriple(IRI subject, IRI predicate, String object, IRI datatype) {
        try {
            statementWithLiteralValue(subject.toString(), predicate.toString(), object, null, datatype.toString());
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


