package org.semanticweb.sparql.owlbgp.model;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubPropertyChainOfAxiom;

import uk.ac.manchester.cs.owl.owlapi.turtle.parser.ConsoleTripleHandler;
import uk.ac.manchester.cs.owl.owlapi.turtle.parser.OWLRDFConsumerAdapter;
import uk.ac.manchester.cs.owl.owlapi.turtle.parser.ParseException;
import uk.ac.manchester.cs.owl.owlapi.turtle.parser.TurtleParser;

public class TestOWLAPIParser extends TestCase {
    public static final String LB = System.getProperty("line.separator") ;
    
    public TestOWLAPIParser() {
        super();
    }
    public TestOWLAPIParser(String name) {
        super(name);
    }
    public void testChainAxiomWithAnnotation() throws Exception {
        String s="_:ont a <http://www.w3.org/2002/07/owl#Ontology> ."
            + "_:x <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/2002/07/owl#Axiom> ." +LB
            + "_:x <http://www.w3.org/2002/07/owl#annotatedSource> <http://example.org/myOnt/hasAunt> ."+LB
            + "_:x <http://www.w3.org/2002/07/owl#annotatedProperty> <http://www.w3.org/2002/07/owl#propertyChainAxiom> ."+LB
            + "_:x <http://www.w3.org/2002/07/owl#annotatedTarget> _:y1 ."+LB
            + "_:x <http://www.w3.org/2000/01/rdf-schema#comment> \"An aunt is a mother's sister.\" ."+LB
            + "<http://example.org/myOnt/hasAunt> <http://www.w3.org/2002/07/owl#propertyChainAxiom> _:y1 ."+LB
            + "_:y1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#first> <http://example.org/myOnt/hasMother> ."+LB
            + "_:y1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#rest> _:y2 ."+LB
            + "_:y2 <http://www.w3.org/1999/02/22-rdf-syntax-ns#first> <http://example.org/myOnt/hasSister> ."+LB
            + "_:y2 <http://www.w3.org/1999/02/22-rdf-syntax-ns#rest> <http://www.w3.org/1999/02/22-rdf-syntax-ns#nil> . "+LB
            + "<http://example.org/myOnt/hasAunt> a <http://www.w3.org/2002/07/owl#ObjectProperty> ."+LB
            + "<http://example.org/myOnt/hasMother> a <http://www.w3.org/2002/07/owl#ObjectProperty> . "+LB
            + "<http://example.org/myOnt/hasSister> a <http://www.w3.org/2002/07/owl#ObjectProperty> .";
        OWLOntologyManager manager=OWLManager.createOWLOntologyManager();
        OWLDataFactory df=manager.getOWLDataFactory();
//        OWLOntology ontology=manager.loadOntologyFromOntologyDocument(new ByteArrayInputStream(s.getBytes()));
        OWLOntology ontology=manager.loadOntologyFromOntologyDocument(IRI.create("file:/Users/bglimm/Downloads/turtletest.tur"));
        for (OWLAxiom ax : ontology.getAxioms()) {
            System.out.println(ax.toString());
        }
        OWLObjectProperty hasAunt=df.getOWLObjectProperty(IRI.create("http://example.org/myOnt/hasAunt"));
        OWLObjectProperty hasMother=df.getOWLObjectProperty(IRI.create("http://example.org/myOnt/hasMother"));
        OWLObjectProperty hasSister=df.getOWLObjectProperty(IRI.create("http://example.org/myOnt/hasSister"));
        OWLAnnotationProperty comment=df.getOWLAnnotationProperty(IRI.create("http://www.w3.org/2000/01/rdf-schema#comment"));
        OWLAnnotation anno=df.getOWLAnnotation(comment, df.getOWLLiteral("http://example.org/myOnt/Seth_MacFarlane",""));
        List<OWLObjectProperty> chain=new ArrayList<OWLObjectProperty>();
        chain.add(hasMother);
        chain.add(hasSister);
        OWLSubPropertyChainOfAxiom sub=df.getOWLSubPropertyChainOfAxiom(chain, hasAunt, Collections.singleton(anno)); 
        assertTrue(ontology.containsAxiom(sub));
    }
    protected void parseOntology(String s, OWLOntology ontology, OWLOntologyManager manager) {
        TurtleParser parser=new TurtleParser(new StringReader(s), new ConsoleTripleHandler(), "");
        OWLRDFConsumerAdapter consumer = new OWLRDFConsumerAdapter(manager, ontology, parser);
        parser.setTripleHandler(consumer);
        try {
            parser.parseDocument();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}