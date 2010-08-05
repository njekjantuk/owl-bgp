package org.semanticweb.sparql.owlbgp.parser;

import java.io.StringReader;
import java.util.Collections;

import junit.framework.TestCase;

import org.semanticweb.sparql.owlbgp.model.Annotation;
import org.semanticweb.sparql.owlbgp.model.IRI;
import org.semanticweb.sparql.owlbgp.model.Ontology;
import org.semanticweb.sparql.owlbgp.model.axioms.NegativeObjectPropertyAssertion;
import org.semanticweb.sparql.owlbgp.model.individuals.NamedIndividual;
import org.semanticweb.sparql.owlbgp.model.properties.AnnotationProperty;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectProperty;
import org.semanticweb.sparql.owlbgp.parser.OWLBGPParser;
import org.semanticweb.sparql.owlbgp.parser.ParseException;

public class TestParser extends TestCase {
    public static final String LB = System.getProperty("line.separator") ;
    
    public TestParser() {
        super();
    }
    public TestParser(String name) {
        super(name);
    }
//    public void testParseOntology() {
//        String s="<http://example.org/myOnt> rdf:type owl:Ontology ."+LB
//            + "<http://example.org/myOnt> owl:versionIRI <http://example.org/myOnt/1.0> ."+LB
//            + "<http://example.org/myOnt> owl:versionIRI <http://example.org/myOnt/2.0> ."+LB
//            + "<http://example.org/myOnt> owl:imports <http://example.org/myOtherOnt> ."+LB
//            + "<http://example.org/myOnt> owl:imports <http://example.org/anotherOnt> ."+LB
//            + "<http://example.org/myOnt/C1> rdf:type owl:Class . "+LB
//            + "<http://example.org/myOnt/C2> rdf:type owl:Class . "+LB
//            + "<http://example.org/myOnt/C1> rdfs:subClassOf <http://example.org/myOnt/C2> . ";
//        Ontology ontology=parseOntology(s);
//        assertTrue(ontology.containsImport(Import.create(IRI.create("http://example.org/anotherOnt"))));
//        assertTrue(ontology.containsImport(Import.create(IRI.create("http://example.org/myOtherOnt"))));
//        assertTrue(ontology.containsAxiom(Declaration.create(Clazz.create("http://example.org/myOnt/C1"))));
//        assertTrue(ontology.containsAxiom(Declaration.create(Clazz.create("http://example.org/myOnt/C2"))));
//        assertTrue(ontology.containsAxiom(SubClassOf.create(Clazz.create("http://example.org/myOnt/C1"), Clazz.create("http://example.org/myOnt/C2"))));
//        assertTrue(ontology.containsVersionIRI(IRI.create("http://example.org/myOnt/2.0")));
//        assertTrue(ontology.containsVersionIRI(IRI.create("http://example.org/myOnt/1.0")));
//        assertTrue(ontology.m_IRI==IRI.create("http://example.org/myOnt"));
//        assertTrue(ontology.getAxioms().size()==3);
//    }
//    public void testParseOntologyWithVars() {
//        String s="?ontIRI rdf:type owl:Ontology ."+LB
//            + "?ontIRI owl:versionIRI ?versionIRI ."+LB
//            + "?ontIRI owl:versionIRI <http://example.org/myOnt/2.0> ."+LB
//            + "<http://example.org/myOnt> owl:imports ?imports ."+LB
//            + "<http://example.org/myOnt> owl:imports <http://example.org/anotherOnt> ."+LB
//            + "?class rdf:type owl:Class . "+LB
//            + "<http://example.org/myOnt/C2> rdf:type owl:Class . "+LB
//            + "?class rdfs:subClassOf <http://example.org/myOnt/C2> . ";
//        Ontology ontology=parseOntology(s);
//        assertTrue(ontology.containsImport(Import.create(IRI.create("http://example.org/anotherOnt"))));
//        assertTrue(ontology.containsImport(Import.create(UntypedVariable.create("?imports"))));
//        assertTrue(ontology.containsAxiom(Declaration.create(ClassVariable.create("?class"))));
//        assertTrue(ontology.containsAxiom(Declaration.create(Clazz.create("http://example.org/myOnt/C2"))));
//        assertTrue(ontology.containsAxiom(SubClassOf.create(ClassVariable.create("?class"), Clazz.create("http://example.org/myOnt/C2"))));
//        assertTrue(ontology.containsVersionIRI(IRI.create("http://example.org/myOnt/2.0")));
//        assertTrue(ontology.containsVersionIRI(UntypedVariable.create("?versionIRI")));
//        assertTrue(ontology.getOntologyIRI()==UntypedVariable.create("?ontIRI"));
//        assertTrue(ontology.getAxioms().size()==3);
//    }
//    public void testObjectInverseOf() {
//        String s="_:x owl:inverseOf <http://example.org/myOnt/op1> ."+LB
//            + "_:x rdfs:subPropertyOf <http://example.org/myOnt/op2> . "+LB
//            + "<http://example.org/myOnt/op2> rdf:type owl:ObjectProperty . "+LB
//            + "<http://example.org/myOnt/op1> rdf:type owl:ObjectProperty . ";
//        Ontology ontology=parseOntology(s);
//        ObjectProperty op2=ObjectProperty.create("http://example.org/myOnt/op2");
//        ObjectProperty op1=ObjectProperty.create("http://example.org/myOnt/op1");
//        assertTrue(ontology.containsAxiom(Declaration.create(op1)));
//        assertTrue(ontology.containsAxiom(Declaration.create(op2)));
//        assertTrue(ontology.containsAxiom(SubObjectPropertyOf.create(ObjectInverseOf.create(op1), op2)));
//        assertTrue(ontology.getAxioms().size()==3);
//    }
//    public void testObjectInverseOfWithVars() {
//        String s="_:x owl:inverseOf ?op1 ."+LB
//            + "_:x rdfs:subPropertyOf <http://example.org/myOnt/op2> . "+LB
//            + "<http://example.org/myOnt/op2> rdf:type owl:ObjectProperty . "+LB
//            + "$op1 rdf:type owl:ObjectProperty . ";
//        Ontology ontology=parseOntology(s);
//        ObjectProperty op2=ObjectProperty.create("http://example.org/myOnt/op2");
//        ObjectPropertyVariable op1=ObjectPropertyVariable.create("$op1");
//        assertTrue(ontology.containsAxiom(Declaration.create(op1)));
//        assertTrue(ontology.containsAxiom(Declaration.create(op2)));
//        assertTrue(ontology.containsAxiom(SubObjectPropertyOf.create(ObjectInverseOf.create(op1), op2)));
//        assertTrue(ontology.getAxioms().size()==3);
//    }
//    public void testAnnotationProperty() {
//        String s="<http://example.org/myOnt/ap1> a owl:AnnotationProperty ."+LB
//            + "<http://example.org/myOnt/ap1> rdfs:range <http://example.org/myOnt/range> . ";
//        Ontology ontology=parseOntology(s);
//        AnnotationProperty ap1=AnnotationProperty.create("http://example.org/myOnt/ap1");
//        IRI range=IRI.create("http://example.org/myOnt/range");
//        assertTrue(ontology.containsAxiom(Declaration.create(ap1)));
//        assertTrue(ontology.containsAxiom(AnnotationPropertyRange.create(ap1, range)));
//        assertTrue(ontology.getAxioms().size()==2);
//    }
//    public void testAnnotationPropertyWithVars() {
//        String s="?ap a owl:AnnotationProperty ."+LB
//            + "?ap rdfs:range <http://example.org/myOnt/range> . ";
//        Ontology ontology=parseOntology(s);
//        AnnotationPropertyVariable ap1=AnnotationPropertyVariable.create("?ap");
//        IRI range=IRI.create("<http://example.org/myOnt/range>");
//        assertTrue(ontology.containsAxiom(Declaration.create(ap1)));
//        assertTrue(ontology.containsAxiom(AnnotationPropertyRange.create(ap1, range)));
//        assertTrue(ontology.getAxioms().size()==2);
//    }
//    public void testDataIntersection() {
//        String s="<http://example.org/myOnt/class> rdfs:subClassOf _:class . "+LB
//            + "_:class rdf:type owl:Restriction ."+LB
//            + "_:class owl:onProperty <http://example.org/myOnt/dp> ."+LB
//            + "_:class owl:allValuesFrom _:dr . "+LB
//            + "_:dr rdf:type rdfs:Datatype . "+LB
//            + "_:dr owl:intersectionOf ( xsd:int xsd:decimal <http://www.w3.org/2001/XMLSchema#byte>) . "
//            + "<http://example.org/myOnt/dp> a owl:DataProperty . ";
//        Ontology ontology=parseOntology(s);
//        Clazz sub=Clazz.create("http://example.org/myOnt/class");
//        DataProperty dp=DataProperty.create("http://example.org/myOnt/dp");
//        Datatype intDT=Datatype.create("http://www.w3.org/2001/XMLSchema#int");
//        Datatype decimalDT=Datatype.create("<http://www.w3.org/2001/XMLSchema#decimal>");
//        Datatype byteDT=Datatype.OWL2_DATATYPES.BYTE.getDatatype();
//        DataIntersectionOf and=DataIntersectionOf.create(decimalDT, byteDT, intDT);
//        assertTrue(ontology.containsAxiom(Declaration.create(dp)));
//        assertTrue(ontology.containsAxiom(SubClassOf.create(sub, DataAllValuesFrom.create(dp, and))));
//        assertTrue(ontology.getAxioms().size()==2);
//    }
//    public void testDataIntersectionWithVars() {
//        String s="_:ont rdf:type owl:Ontology . "+LB
//            + "$class rdfs:subClassOf _:class . "+LB
//            + "_:class rdf:type owl:Restriction ."+LB
//            + "_:class owl:onProperty ?dp ."+LB
//            + "_:class owl:allValuesFrom _:dr . "+LB
//            + "_:dr rdf:type rdfs:Datatype . "+LB
//            + "_:dr owl:intersectionOf ( ?dtint xsd:decimal ?dtbyte) . "
//            + "?dp a owl:DataProperty . "
//            + "?class rdf:type owl:Class . "
//            + "?dtint a rdfs:Datatype . "
//            + "?dtbyte a rdfs:Datatype . ";
//        Ontology ontology=parseOntology(s);
//        ClassVariable sub=ClassVariable.create("?class");
//        DataPropertyVariable dp=DataPropertyVariable.create("$dp");
//        DatatypeVariable intDT=DatatypeVariable.create("?dtint");
//        Datatype decimalDT=Datatype.create("<http://www.w3.org/2001/XMLSchema#decimal>");
//        DatatypeVariable byteDT=DatatypeVariable.create("$dtbyte");
//        DataIntersectionOf and=DataIntersectionOf.create(decimalDT, byteDT, intDT);
//        assertTrue(ontology.containsAxiom(Declaration.create(dp)));
//        assertTrue(ontology.containsAxiom(Declaration.create(sub)));
//        assertTrue(ontology.containsAxiom(Declaration.create(intDT)));
//        assertTrue(ontology.containsAxiom(Declaration.create(byteDT)));
//        assertTrue(ontology.containsAxiom(SubClassOf.create(sub, DataAllValuesFrom.create(dp, and))));
//        assertTrue(ontology.getAxioms().size()==5);
//    }
    public void testMultiTripleAxiomWithAnnotation() {
        String s="_:x rdf:type owl:NegativePropertyAssertion ."
            + "_:x owl:sourceIndividual <http://example.org/myOnt/Chris> ."
            + "_:x owl:assertionProperty <http://example.org/myOnt/brotherOf> ."
            + "_:x owl:targetIndividual <http://example.org/myOnt/Stewie> ."
            + "_:x <http://example.org/myOnt/author> <http://example.org/myOnt/Seth_MacFarlane> ."
            + "<http://example.org/myOnt/Chris> a owl:NamedIndividual ."
            + "<http://example.org/myOnt/brotherOf> a owl:ObjectProperty. "
            + "<http://example.org/myOnt/Stewie> a owl:NamedIndividual ."
            + "<http://example.org/myOnt/author> a owl:AnnotationProperty .";
        Ontology ontology=parseOntology(s);
        NamedIndividual chris=NamedIndividual.create("http://example.org/myOnt/Chris");
        NamedIndividual stewie=NamedIndividual.create("http://example.org/myOnt/Stewie");
        ObjectProperty brotherOf=ObjectProperty.create("http://example.org/myOnt/brotherOf");
        AnnotationProperty author=AnnotationProperty.create("http://example.org/myOnt/author");
        Annotation anno=Annotation.create(author, IRI.create("http://example.org/myOnt/Seth_MacFarlane"));
        NegativeObjectPropertyAssertion assertion=NegativeObjectPropertyAssertion.create(brotherOf, chris, stewie, Collections.singleton(anno)); 
        assertTrue(ontology.containsAxiom(assertion));
    }
//    public void testChainAxiomWithAnnotation() {
//        String s="_:x rdf:type owl:Axiom ."
//            + "_:x owl:annotatedSource <http://example.org/myOnt/hasAunt> ."
//            + "_:x owl:annotatedProperty owl:propertyChainAxiom ."
//            + "_:x owl:annotatedTarget _:y1 ."
//            + "_:x rdfs:comment \"An aunt is a mother's sister.\" ."
//            + "<http://example.org/myOnt/hasAunt> owl:propertyChainAxiom _:y1 ."
//            + "_:y1 rdf:first <http://example.org/myOnt/hasMother> ."
//            + "_:y1 rdf:rest _:y2 ."
//            + "_:y2 rdf:first <http://example.org/myOnt/hasSister> ."
//            + "_:y2 rdf:rest rdf:nil . "
//            + "<http://example.org/myOnt/hasAunt> a owl:ObjectProperty ."
//            + "<http://example.org/myOnt/hasMother> a owl:ObjectProperty. "
//            + "<http://example.org/myOnt/hasSister> a owl:ObjectProperty .";
//        Ontology ontology=parseOntology(s);
//        ObjectProperty hasAunt=ObjectProperty.create("http://example.org/myOnt/hasAunt");
//        ObjectProperty hasMother=ObjectProperty.create("http://example.org/myOnt/hasMother");
//        ObjectProperty hasSister=ObjectProperty.create("http://example.org/myOnt/hasSister");
//        AnnotationProperty comment=AnnotationProperty.create("http://www.w3.org/2000/01/rdf-schema#comment");
//        Annotation anno=Annotation.create(comment, TypedLiteral.create("An aunt is a mother's sister."));
//        SubObjectPropertyOf sub=SubObjectPropertyOf.create(ObjectPropertyChain.create(hasMother, hasSister), hasAunt, Collections.singleton(anno)); 
//        assertTrue(ontology.containsAxiom(sub));
//    }
//    public void testChainAxiomWithAnnotatedAnnotations() {
//        String s="_:x rdf:type owl:Axiom ."
//            + "_:x owl:annotatedSource <http://example.org/myOnt/hasAunt> ."
//            + "_:x owl:annotatedProperty owl:propertyChainAxiom ."
//            + "_:x owl:annotatedTarget _:y1 ."
//            + "_:x rdfs:comment \"An aunt is a mother's sister.\" ."
//            + "_:y rdf:type owl:Annotation ."
//            + "_:y owl:annotatedSource _:x ."
//            + "_:y owl:annotatedProperty rdfs:comment ."
//            + "_:y owl:annotatedTarget \"An aunt is a mother's sister.\" ."
//            + "_:y rdfs:label \"Annotation annotation.\" ."
//            + "<http://example.org/myOnt/hasAunt> owl:propertyChainAxiom _:y1 ."
//            + "_:y1 rdf:first <http://example.org/myOnt/hasMother> ."
//            + "_:y1 rdf:rest _:y2 ."
//            + "_:y2 rdf:first <http://example.org/myOnt/hasSister> ."
//            + "_:y2 rdf:rest rdf:nil . "
//            + "<http://example.org/myOnt/hasAunt> a owl:ObjectProperty ."
//            + "<http://example.org/myOnt/hasMother> a owl:ObjectProperty. "
//            + "<http://example.org/myOnt/hasSister> a owl:ObjectProperty .";
//        Ontology ontology=parseOntology(s);
//        ObjectProperty hasAunt=ObjectProperty.create("http://example.org/myOnt/hasAunt");
//        ObjectProperty hasMother=ObjectProperty.create("http://example.org/myOnt/hasMother");
//        ObjectProperty hasSister=ObjectProperty.create("http://example.org/myOnt/hasSister");
//        AnnotationProperty comment=AnnotationProperty.create("http://www.w3.org/2000/01/rdf-schema#comment");
//        AnnotationProperty label=AnnotationProperty.create("http://www.w3.org/2000/01/rdf-schema#label");
//        Annotation annoAnno=Annotation.create(label, TypedLiteral.create("Annotation annotation."));
//        Annotation anno=Annotation.create(comment, TypedLiteral.create("An aunt is a mother's sister."), Collections.singleton(annoAnno));
//        SubObjectPropertyOf sub=SubObjectPropertyOf.create(ObjectPropertyChain.create(hasMother, hasSister), hasAunt, Collections.singleton(anno)); 
//        assertTrue(ontology.containsAxiom(sub));
//    }
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