package org.semanticweb.sparql.owlbgp.model;

import java.io.StringReader;

import junit.framework.TestCase;

import org.semanticweb.sparql.owlbgp.model.axioms.Axiom;
import org.semanticweb.sparql.owlbgpparser.OWLBGPParser;
import org.semanticweb.sparql.owlbgpparser.ParseException;

public class TestParser extends TestCase {

    public TestParser() {
        super();
    }
    public TestParser(String name) {
        super(name);
    }
    public void testParseOntology() {
        String s="<http://example.org/myOnt> rdf:type owl:Ontology ."
            + "<http://example.org/myOnt> owl:versionIRI <http://example.org/myOnt/1.0> ."
            + "<http://example.org/myOnt> owl:versionIRI <http://example.org/myOnt/2.0> ."
            + "<http://example.org/myOnt> owl:imports <http://example.org/myOtherOnt> ."
            + "<http://example.org/myOnt> owl:imports <http://example.org/anotherOnt> ."
            + "<http://example.org/myOnt/C1> rdf:type owl:Class . "
            + "<http://example.org/myOnt/C2> rdf:type owl:Class . "
            + "<http://example.org/myOnt/C1> rdfs:subClassOf <http://example.org/myOnt/C2> . ";
        OWLBGPParser parser=new OWLBGPParser(new StringReader(s));
        try {
            parser.parse();
            for (Axiom axiom : parser.getParsedAxioms())
                System.out.println(axiom);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}