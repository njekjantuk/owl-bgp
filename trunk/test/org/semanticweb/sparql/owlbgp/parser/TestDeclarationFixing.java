package org.semanticweb.sparql.owlbgp.parser;

import java.io.StringReader;

import org.semanticweb.sparql.owlbgp.AbstractTest;
import org.semanticweb.sparql.owlbgp.model.axioms.Declaration;
import org.semanticweb.sparql.owlbgp.model.axioms.InverseFunctionalObjectProperty;
import org.semanticweb.sparql.owlbgp.model.axioms.SymmetricObjectProperty;
import org.semanticweb.sparql.owlbgp.model.axioms.TransitiveObjectProperty;
import org.semanticweb.sparql.owlbgp.model.properties.AnnotationProperty;

public class TestDeclarationFixing extends AbstractTest {

    public void testDeclarations() throws Exception {
        String s="<http://example.org/ontProp> a owl:OntologyProperty ."
            + "<http://example.org/invFuncProp> a owl:InverseFunctionalProperty ."
            + "<http://example.org/transProp> rdf:type owl:TransitiveProperty ."
            + "<http://example.org/symProp> a owl:SymmetricProperty .";
        OWLBGPParser parser=new OWLBGPParser(new StringReader(s));
        TripleConsumer consumer=parser.handler;
        parser.parse();
        assertTrue(consumer.getAxioms().contains(Declaration.create(AnnotationProperty.create(IRI("ontProp")))));
        assertTrue(!consumer.containsTriple(IRI("ontProp"), Vocabulary.RDF_TYPE, Vocabulary.OWL_ONTOLOGY_PROPERTY));
        assertTrue(consumer.getAxioms().contains(Declaration.create(OP("invFuncProp"))));
        assertTrue(consumer.getAxioms().contains(InverseFunctionalObjectProperty.create(OP("invFuncProp"))));
        assertTrue(consumer.getAxioms().contains(Declaration.create(OP("transProp"))));
        assertTrue(consumer.getAxioms().contains(TransitiveObjectProperty.create(OP("transProp"))));
        assertTrue(consumer.getAxioms().contains(Declaration.create(OP("symProp"))));
        assertTrue(consumer.getAxioms().contains(SymmetricObjectProperty.create(OP("symProp"))));
        assertNoTriplesLeft(consumer);
    }
}