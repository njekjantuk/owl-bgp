package org.semanticweb.sparql.owlbgp.model;

import java.io.StringReader;

import junit.framework.TestCase;

import org.semanticweb.sparql.owlbgp.model.axioms.AnnotationPropertyRange;
import org.semanticweb.sparql.owlbgp.model.axioms.SubClassOf;
import org.semanticweb.sparql.owlbgp.model.axioms.SubObjectPropertyOf;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassVariable;
import org.semanticweb.sparql.owlbgp.model.classexpressions.Clazz;
import org.semanticweb.sparql.owlbgp.model.classexpressions.DataAllValuesFrom;
import org.semanticweb.sparql.owlbgp.model.dataranges.DataIntersectionOf;
import org.semanticweb.sparql.owlbgp.model.dataranges.Datatype;
import org.semanticweb.sparql.owlbgp.model.dataranges.DatatypeVariable;
import org.semanticweb.sparql.owlbgp.model.properties.AnnotationProperty;
import org.semanticweb.sparql.owlbgp.model.properties.AnnotationPropertyVariable;
import org.semanticweb.sparql.owlbgp.model.properties.DataProperty;
import org.semanticweb.sparql.owlbgp.model.properties.DataPropertyVariable;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectInverseOf;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectProperty;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyVariable;
import org.semanticweb.sparql.owlbgpparser.OWLBGPParser;
import org.semanticweb.sparql.owlbgpparser.ParseException;

public class TestParser extends TestCase {
    public static final String LB = System.getProperty("line.separator") ;
    
    public TestParser() {
        super();
    }
    public TestParser(String name) {
        super(name);
    }
    public void testParseOntology() {
        String s="<http://example.org/myOnt> rdf:type owl:Ontology ."+LB
            + "<http://example.org/myOnt> owl:versionIRI <http://example.org/myOnt/1.0> ."+LB
            + "<http://example.org/myOnt> owl:versionIRI <http://example.org/myOnt/2.0> ."+LB
            + "<http://example.org/myOnt> owl:imports <http://example.org/myOtherOnt> ."+LB
            + "<http://example.org/myOnt> owl:imports <http://example.org/anotherOnt> ."+LB
            + "<http://example.org/myOnt/C1> rdf:type owl:Class . "+LB
            + "<http://example.org/myOnt/C2> rdf:type owl:Class . "+LB
            + "<http://example.org/myOnt/C1> rdfs:subClassOf <http://example.org/myOnt/C2> . ";
        Ontology ontology=parseOntology(s);
        assertTrue(ontology.containsImport(Import.create(IRI.create("http://example.org/anotherOnt"))));
        assertTrue(ontology.containsImport(Import.create(IRI.create("http://example.org/myOtherOnt"))));
        assertTrue(ontology.containsAxiom(Declaration.create(Clazz.create("http://example.org/myOnt/C1"))));
        assertTrue(ontology.containsAxiom(Declaration.create(Clazz.create("http://example.org/myOnt/C2"))));
        assertTrue(ontology.containsAxiom(SubClassOf.create(Clazz.create("http://example.org/myOnt/C1"), Clazz.create("http://example.org/myOnt/C2"))));
        assertTrue(ontology.containsVersionIRI(IRI.create("http://example.org/myOnt/2.0")));
        assertTrue(ontology.containsVersionIRI(IRI.create("http://example.org/myOnt/1.0")));
        assertTrue(ontology.m_IRI==IRI.create("http://example.org/myOnt"));
        assertTrue(ontology.getAxioms().size()==3);
    }
    public void testParseOntologyWithVars() {
        String s="?ontIRI rdf:type owl:Ontology ."+LB
            + "?ontIRI owl:versionIRI ?versionIRI ."+LB
            + "?ontIRI owl:versionIRI <http://example.org/myOnt/2.0> ."+LB
            + "<http://example.org/myOnt> owl:imports ?imports ."+LB
            + "<http://example.org/myOnt> owl:imports <http://example.org/anotherOnt> ."+LB
            + "?class rdf:type owl:Class . "+LB
            + "<http://example.org/myOnt/C2> rdf:type owl:Class . "+LB
            + "?class rdfs:subClassOf <http://example.org/myOnt/C2> . ";
        Ontology ontology=parseOntology(s);
        assertTrue(ontology.containsImport(Import.create(IRI.create("http://example.org/anotherOnt"))));
        assertTrue(ontology.containsImport(Import.create(UntypedVariable.create("?imports"))));
        assertTrue(ontology.containsAxiom(Declaration.create(ClassVariable.create("?class"))));
        assertTrue(ontology.containsAxiom(Declaration.create(Clazz.create("http://example.org/myOnt/C2"))));
        assertTrue(ontology.containsAxiom(SubClassOf.create(ClassVariable.create("?class"), Clazz.create("http://example.org/myOnt/C2"))));
        assertTrue(ontology.containsVersionIRI(IRI.create("http://example.org/myOnt/2.0")));
        assertTrue(ontology.containsVersionIRI(UntypedVariable.create("?versionIRI")));
        assertTrue(ontology.getOntologyIRI()==UntypedVariable.create("?ontIRI"));
        assertTrue(ontology.getAxioms().size()==3);
    }
    public void testObjectInverseOf() {
        String s="_:x owl:inverseOf <http://example.org/myOnt/op1> ."+LB
            + "_:x rdfs:subPropertyOf <http://example.org/myOnt/op2> . "+LB
            + "<http://example.org/myOnt/op2> rdf:type owl:ObjectProperty . "+LB
            + "<http://example.org/myOnt/op1> rdf:type owl:ObjectProperty . ";
        Ontology ontology=parseOntology(s);
        ObjectProperty op2=ObjectProperty.create("http://example.org/myOnt/op2");
        ObjectProperty op1=ObjectProperty.create("http://example.org/myOnt/op1");
        assertTrue(ontology.containsAxiom(Declaration.create(op1)));
        assertTrue(ontology.containsAxiom(Declaration.create(op2)));
        assertTrue(ontology.containsAxiom(SubObjectPropertyOf.create(ObjectInverseOf.create(op1), op2)));
        assertTrue(ontology.getAxioms().size()==3);
    }
    public void testObjectInverseOfWithVars() {
        String s="_:x owl:inverseOf ?op1 ."+LB
            + "_:x rdfs:subPropertyOf <http://example.org/myOnt/op2> . "+LB
            + "<http://example.org/myOnt/op2> rdf:type owl:ObjectProperty . "+LB
            + "$op1 rdf:type owl:ObjectProperty . ";
        Ontology ontology=parseOntology(s);
        ObjectProperty op2=ObjectProperty.create("http://example.org/myOnt/op2");
        ObjectPropertyVariable op1=ObjectPropertyVariable.create("$op1");
        assertTrue(ontology.containsAxiom(Declaration.create(op1)));
        assertTrue(ontology.containsAxiom(Declaration.create(op2)));
        assertTrue(ontology.containsAxiom(SubObjectPropertyOf.create(ObjectInverseOf.create(op1), op2)));
        assertTrue(ontology.getAxioms().size()==3);
    }
    public void testAnnotationProperty() {
        String s="<http://example.org/myOnt/ap1> a owl:AnnotationProperty ."+LB
            + "<http://example.org/myOnt/ap1> rdfs:range <http://example.org/myOnt/range> . ";
        Ontology ontology=parseOntology(s);
        AnnotationProperty ap1=AnnotationProperty.create("http://example.org/myOnt/ap1");
        IRI range=IRI.create("http://example.org/myOnt/range");
        assertTrue(ontology.containsAxiom(Declaration.create(ap1)));
        assertTrue(ontology.containsAxiom(AnnotationPropertyRange.create(ap1, range)));
        assertTrue(ontology.getAxioms().size()==2);
    }
    public void testAnnotationPropertyWithVars() {
        String s="?ap a owl:AnnotationProperty ."+LB
            + "?ap rdfs:range <http://example.org/myOnt/range> . ";
        Ontology ontology=parseOntology(s);
        AnnotationPropertyVariable ap1=AnnotationPropertyVariable.create("?ap");
        IRI range=IRI.create("<http://example.org/myOnt/range>");
        assertTrue(ontology.containsAxiom(Declaration.create(ap1)));
        assertTrue(ontology.containsAxiom(AnnotationPropertyRange.create(ap1, range)));
        assertTrue(ontology.getAxioms().size()==2);
    }
    public void testDataIntersection() {
        String s="<http://example.org/myOnt/class> rdfs:subClassOf _:class . "+LB
            + "_:class rdf:type owl:Restriction ."+LB
            + "_:class owl:onProperty <http://example.org/myOnt/dp> ."+LB
            + "_:class owl:allValuesFrom _:dr . "+LB
            + "_:dr rdf:type rdfs:Datatype . "+LB
            + "_:dr owl:intersectionOf ( xsd:int xsd:decimal <http://www.w3.org/2001/XMLSchema#byte>) . "
            + "<http://example.org/myOnt/dp> a owl:DataProperty . ";
        Ontology ontology=parseOntology(s);
        Clazz sub=Clazz.create("http://example.org/myOnt/class");
        DataProperty dp=DataProperty.create("http://example.org/myOnt/dp");
        Datatype intDT=Datatype.create("http://www.w3.org/2001/XMLSchema#int");
        Datatype decimalDT=Datatype.create("<http://www.w3.org/2001/XMLSchema#decimal>");
        Datatype byteDT=Datatype.OWL2_DATATYPES.BYTE.getDatatype();
        DataIntersectionOf and=DataIntersectionOf.create(decimalDT, byteDT, intDT);
        assertTrue(ontology.containsAxiom(Declaration.create(dp)));
        assertTrue(ontology.containsAxiom(SubClassOf.create(sub, DataAllValuesFrom.create(dp, and))));
        assertTrue(ontology.getAxioms().size()==2);
    }
    public void testDataIntersectionWithVars() {
        String s="_:ont rdf:type owl:Ontology . "+LB
            + "$class rdfs:subClassOf _:class . "+LB
            + "_:class rdf:type owl:Restriction ."+LB
            + "_:class owl:onProperty ?dp ."+LB
            + "_:class owl:allValuesFrom _:dr . "+LB
            + "_:dr rdf:type rdfs:Datatype . "+LB
            + "_:dr owl:intersectionOf ( ?dtint xsd:decimal ?dtbyte) . "
            + "?dp a owl:DataProperty . "
            + "?class rdf:type owl:Class . "
            + "?dtint a rdfs:Datatype . "
            + "?dtbyte a rdfs:Datatype . ";
        Ontology ontology=parseOntology(s);
        ClassVariable sub=ClassVariable.create("?class");
        DataPropertyVariable dp=DataPropertyVariable.create("$dp");
        DatatypeVariable intDT=DatatypeVariable.create("?dtint");
        Datatype decimalDT=Datatype.create("<http://www.w3.org/2001/XMLSchema#decimal>");
        DatatypeVariable byteDT=DatatypeVariable.create("$dtbyte");
        DataIntersectionOf and=DataIntersectionOf.create(decimalDT, byteDT, intDT);
        assertTrue(ontology.containsAxiom(Declaration.create(dp)));
        assertTrue(ontology.containsAxiom(Declaration.create(sub)));
        assertTrue(ontology.containsAxiom(Declaration.create(intDT)));
        assertTrue(ontology.containsAxiom(Declaration.create(byteDT)));
        assertTrue(ontology.containsAxiom(SubClassOf.create(sub, DataAllValuesFrom.create(dp, and))));
        assertTrue(ontology.getAxioms().size()==5);
    }
    protected Ontology parseOntology(String s) {
        OWLBGPParser parser=new OWLBGPParser(new StringReader(s));
        Ontology ontology=null;
        try {
            parser.parse();
            ontology=parser.getParsedOntology();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ontology;
    }
}