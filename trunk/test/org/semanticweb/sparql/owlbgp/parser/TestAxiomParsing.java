package org.semanticweb.sparql.owlbgp.parser;

import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.sparql.owlbgp.AbstractTest;
import org.semanticweb.sparql.owlbgp.model.Annotation;
import org.semanticweb.sparql.owlbgp.model.axioms.Axiom;
import org.semanticweb.sparql.owlbgp.model.axioms.Declaration;
import org.semanticweb.sparql.owlbgp.model.axioms.DifferentIndividuals;
import org.semanticweb.sparql.owlbgp.model.axioms.DisjointClasses;
import org.semanticweb.sparql.owlbgp.model.axioms.DisjointDataProperties;
import org.semanticweb.sparql.owlbgp.model.axioms.DisjointObjectProperties;
import org.semanticweb.sparql.owlbgp.model.axioms.EquivalentClasses;
import org.semanticweb.sparql.owlbgp.model.axioms.NegativeDataPropertyAssertion;
import org.semanticweb.sparql.owlbgp.model.axioms.NegativeObjectPropertyAssertion;
import org.semanticweb.sparql.owlbgp.model.axioms.SubClassOf;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.classexpressions.DataAllValuesFrom;
import org.semanticweb.sparql.owlbgp.model.classexpressions.DataExactCardinality;
import org.semanticweb.sparql.owlbgp.model.classexpressions.DataMaxCardinality;
import org.semanticweb.sparql.owlbgp.model.classexpressions.DataSomeValuesFrom;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ObjectAllValuesFrom;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ObjectComplementOf;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ObjectIntersectionOf;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ObjectMinCardinality;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ObjectSomeValuesFrom;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ObjectUnionOf;
import org.semanticweb.sparql.owlbgp.model.dataranges.DataComplementOf;
import org.semanticweb.sparql.owlbgp.model.dataranges.DataIntersectionOf;
import org.semanticweb.sparql.owlbgp.model.dataranges.DataUnionOf;
import org.semanticweb.sparql.owlbgp.model.dataranges.Datatype;
import org.semanticweb.sparql.owlbgp.model.dataranges.DatatypeRestriction;
import org.semanticweb.sparql.owlbgp.model.dataranges.FacetRestriction;
import org.semanticweb.sparql.owlbgp.model.dataranges.FacetRestriction.OWL2_FACET;
import org.semanticweb.sparql.owlbgp.model.individuals.Individual;
import org.semanticweb.sparql.owlbgp.model.properties.DataPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;

public class TestAxiomParsing extends AbstractTest {
    public static final String LB = System.getProperty("line.separator") ;
    
    public void testEquivalentClassComplex() throws Exception {
        Declaration c=Declaration.create(C("C"));
        Declaration d=Declaration.create(C("D"));
        Declaration r=Declaration.create(OP("r"));
        Axiom sco=EquivalentClasses.create(ObjectSomeValuesFrom.create(IOP("r"), C("C")),ObjectIntersectionOf.create(C("C"),C("D")));
        Declaration e=Declaration.create(C("E"));
        Declaration f=Declaration.create(C("F"));
        Declaration s=Declaration.create(OP("s"));
        Set<ClassExpression> ces=new HashSet<ClassExpression>();
        ces.add(ObjectSomeValuesFrom.create(OP("s"), ObjectComplementOf.create(ObjectIntersectionOf.create(C("E"),C("F")))));
        ces.add(ObjectMinCardinality.create(5, IOP("r")));
        Axiom scoa=EquivalentClasses.create(ces,Annotation.create(AP("rdfs:label"), TL("EFanno")));
        Declaration t=Declaration.create(DP("t"));
        Set<ClassExpression> ces2=new HashSet<ClassExpression>();
        ces2.add(DataAllValuesFrom.create(DP("t"), DataIntersectionOf.create(DT("xsd:string"), DataComplementOf.create(DT("rdf:PlainLiteral")))));
        ces2.add(DataExactCardinality.create(1000, DP("t"), DatatypeRestriction.create(DT("xsd:decimal"), 
                FacetRestriction.create(OWL2_FACET.MAX_EXCLUSIVE, TL("23", null, Datatype.XSD_BYTE)), 
                FacetRestriction.create(OWL2_FACET.MIN_INCLUSIVE, TL("21", null, Datatype.XSD_INTEGER)))));
        Axiom scoaa=EquivalentClasses.create(ces2,Annotation.create(AP("rdfs:comment"), TL("GHAanno"), Annotation.create(AP("rdfs:label"), TL("GHannoAnno"))));
        String str=c.toTurtleString()+d.toTurtleString()+e.toTurtleString()+f.toTurtleString()
            +r.toTurtleString()+s.toTurtleString()+t.toTurtleString()
            +sco.toTurtleString()+scoa.toTurtleString()+scoaa.toTurtleString();
        System.out.println(str);
        OWLBGPParser parser=new OWLBGPParser(new StringReader(str));
        TripleConsumer consumer=parser.handler;
        parser.parse();
        assertTrue(consumer.getAxioms().remove(sco));
        assertTrue(consumer.getAxioms().remove(scoa));
        assertTrue(consumer.getAxioms().remove(scoaa));
        assertTrue(consumer.getAxioms().remove(c));
        assertTrue(consumer.getAxioms().remove(d));
        assertTrue(consumer.getAxioms().remove(e));
        assertTrue(consumer.getAxioms().remove(f));
        assertTrue(consumer.getAxioms().remove(r));
        assertTrue(consumer.getAxioms().remove(s));
        assertTrue(consumer.getAxioms().remove(t));
        assertTrue(consumer.getAxioms().isEmpty());
        assertNoTriplesLeft(consumer);
    }
    public void testEquivalentClasses() throws Exception {
        Declaration c=Declaration.create(C("C"));
        Declaration d=Declaration.create(C("D"));
        Axiom sco=EquivalentClasses.create(C("C"),C("D"));
        Declaration e=Declaration.create(C("E"));
        Declaration f=Declaration.create(C("F"));
        Set<ClassExpression> ces=new HashSet<ClassExpression>();
        ces.add(C("E"));
        ces.add(C("F"));
        Axiom scoa=EquivalentClasses.create(ces,Annotation.create(AP("rdfs:label"), TL("EFanno")));
        Declaration g=Declaration.create(C("G"));
        Declaration h=Declaration.create(C("H"));
        Set<ClassExpression> ces2=new HashSet<ClassExpression>();
        ces2.add(C("G"));
        ces2.add(C("H"));
        Axiom scoaa=EquivalentClasses.create(ces2,Annotation.create(AP("rdfs:comment"), TL("GHAanno"), Annotation.create(AP("rdfs:label"), TL("GHannoAnno"))));
        String s=c.toTurtleString()+d.toTurtleString()+e.toTurtleString()+f.toTurtleString()+g.toTurtleString()+h.toTurtleString()
            +sco.toTurtleString()+scoa.toTurtleString()+scoaa.toTurtleString();
        OWLBGPParser parser=new OWLBGPParser(new StringReader(s));
        TripleConsumer consumer=parser.handler;
        parser.parse();
        assertTrue(consumer.getAxioms().remove(sco));
        assertTrue(consumer.getAxioms().remove(scoa));
        assertTrue(consumer.getAxioms().remove(scoaa));
        assertTrue(consumer.getAxioms().remove(c));
        assertTrue(consumer.getAxioms().remove(d));
        assertTrue(consumer.getAxioms().remove(e));
        assertTrue(consumer.getAxioms().remove(f));
        assertTrue(consumer.getAxioms().remove(g));
        assertTrue(consumer.getAxioms().remove(h));
        assertTrue(consumer.getAxioms().isEmpty());
        assertNoTriplesLeft(consumer);
    }
    public void testSubClassOfComplex() throws Exception {
        Declaration c=Declaration.create(C("C"));
        Declaration d=Declaration.create(C("D"));
        Declaration r=Declaration.create(OP("r"));
        Axiom sco=SubClassOf.create(ObjectSomeValuesFrom.create(IOP("r"), C("C")),ObjectIntersectionOf.create(C("C"),C("D")));
        Declaration e=Declaration.create(C("E"));
        Declaration f=Declaration.create(C("F"));
        Declaration s=Declaration.create(OP("s"));
        Axiom scoa=SubClassOf.create(ObjectAllValuesFrom.create(OP("s"), ObjectUnionOf.create(C("C"), ObjectIntersectionOf.create(C("E"),C("F")))), ObjectMinCardinality.create(5, OP("r"), ObjectIntersectionOf.create(C("E"),C("F"))),Annotation.create(AP("rdfs:label"), TL("EFanno")));
        Declaration t=Declaration.create(DP("t"));
        Axiom scoaa=SubClassOf.create(
                DataSomeValuesFrom.create(DP("t"), DataUnionOf.create(DT("xsd:string"), DT("rdf:PlainLiteral"))), 
                DataMaxCardinality.create(5, DP("t"), DatatypeRestriction.create(DT("xsd:int"), 
                        FacetRestriction.create(OWL2_FACET.MAX_EXCLUSIVE, TL("23", null, Datatype.XSD_BYTE)), 
                        FacetRestriction.create(OWL2_FACET.MIN_INCLUSIVE, TL("21", null, Datatype.XSD_SHORT)))),
                Annotation.create(AP("rdfs:comment"), TL("GHAanno"), Annotation.create(AP("rdfs:label"), TL("GHannoAnno"))));
        String str=c.toTurtleString()+d.toTurtleString()+e.toTurtleString()+f.toTurtleString()
            +r.toTurtleString()+s.toTurtleString()+t.toTurtleString()
            +sco.toTurtleString()+scoa.toTurtleString()+scoaa.toTurtleString();
        OWLBGPParser parser=new OWLBGPParser(new StringReader(str));
        TripleConsumer consumer=parser.handler;
        parser.parse();
        assertTrue(consumer.getAxioms().remove(sco));
        assertTrue(consumer.getAxioms().remove(scoa));
        assertTrue(consumer.getAxioms().remove(scoaa));
        assertTrue(consumer.getAxioms().remove(c));
        assertTrue(consumer.getAxioms().remove(d));
        assertTrue(consumer.getAxioms().remove(e));
        assertTrue(consumer.getAxioms().remove(f));
        assertTrue(consumer.getAxioms().remove(r));
        assertTrue(consumer.getAxioms().remove(s));
        assertTrue(consumer.getAxioms().remove(t));
        assertTrue(consumer.getAxioms().isEmpty());
        assertNoTriplesLeft(consumer);
    }
    public void testSubClassOf() throws Exception {
        Declaration c=Declaration.create(C("C"));
        Declaration d=Declaration.create(C("D"));
        Axiom sco=SubClassOf.create(C("C"),C("D"));
        Declaration e=Declaration.create(C("E"));
        Declaration f=Declaration.create(C("F"));
        Axiom scoa=SubClassOf.create(C("E"),C("F"),Annotation.create(AP("rdfs:label"), TL("EFanno")));
        Declaration g=Declaration.create(C("G"));
        Declaration h=Declaration.create(C("H"));
        Axiom scoaa=SubClassOf.create(C("G"),C("H"),Annotation.create(AP("rdfs:comment"), TL("GHAanno"), Annotation.create(AP("rdfs:label"), TL("GHannoAnno"))));
        String s=c.toTurtleString()+d.toTurtleString()+e.toTurtleString()+f.toTurtleString()+g.toTurtleString()+h.toTurtleString()
            +sco.toTurtleString()+scoa.toTurtleString()+scoaa.toTurtleString();
        OWLBGPParser parser=new OWLBGPParser(new StringReader(s));
        TripleConsumer consumer=parser.handler;
        parser.parse();
        assertTrue(consumer.getAxioms().remove(sco));
        assertTrue(consumer.getAxioms().remove(scoa));
        assertTrue(consumer.getAxioms().remove(scoaa));
        assertTrue(consumer.getAxioms().remove(c));
        assertTrue(consumer.getAxioms().remove(d));
        assertTrue(consumer.getAxioms().remove(e));
        assertTrue(consumer.getAxioms().remove(f));
        assertTrue(consumer.getAxioms().remove(g));
        assertTrue(consumer.getAxioms().remove(h));
        assertTrue(consumer.getAxioms().isEmpty());
        assertNoTriplesLeft(consumer);
    }
    public void testNegativeDataPropertyAssertion() throws Exception {
        Declaration a1=Declaration.create(NI("a1"));
        Declaration r1=Declaration.create(DP("r1"));
        Axiom negDP=NegativeDataPropertyAssertion.create(DP("r1"),NI("a1"),TL("b1"));
        Declaration a2=Declaration.create(NI("a2"));
        Declaration r2=Declaration.create(DP("r2"));
        Axiom negDPa=NegativeDataPropertyAssertion.create(DP("r2"),NI("a2"),TL("b2"),Annotation.create(AP("rdfs:label"), TL("rst2anno")));
        Declaration a3=Declaration.create(NI("a3"));
        Declaration r3=Declaration.create(DP("r3"));
        Axiom negDPaa=NegativeDataPropertyAssertion.create(DP("r3"),NI("a3"),TL("b3"),Annotation.create(AP("rdfs:comment"), TL("rst3Aanno"), Annotation.create(AP("rdfs:label"), TL("rs3annoAnno"))));
        String s=a1.toTurtleString()+r1.toTurtleString()
            +a2.toTurtleString()+r2.toTurtleString()
            +a3.toTurtleString()+r3.toTurtleString()
            +negDP.toTurtleString()+negDPa.toTurtleString()+negDPaa.toTurtleString();
        OWLBGPParser parser=new OWLBGPParser(new StringReader(s));
        TripleConsumer consumer=parser.handler;
        parser.parse();
        assertTrue(consumer.getAxioms().remove(negDP));
        assertTrue(consumer.getAxioms().remove(negDPa));
        assertTrue(consumer.getAxioms().remove(negDPaa));
        assertTrue(consumer.getAxioms().remove(a1));
        assertTrue(consumer.getAxioms().remove(r1));
        assertTrue(consumer.getAxioms().remove(a2));
        assertTrue(consumer.getAxioms().remove(r2));
        assertTrue(consumer.getAxioms().remove(a3));
        assertTrue(consumer.getAxioms().remove(r3));
        assertTrue(consumer.getAxioms().isEmpty());
        assertNoTriplesLeft(consumer);
    }
    public void testNegativeObjectPropertyAssertion() throws Exception {
        Declaration a1=Declaration.create(NI("a1"));
        Declaration b1=Declaration.create(NI("b1"));
        Declaration r1=Declaration.create(OP("r1"));
        Axiom negOP=NegativeObjectPropertyAssertion.create(OP("r1"),NI("a1"),NI("b1"));
        Declaration a2=Declaration.create(NI("a2"));
        Declaration b2=Declaration.create(NI("b2"));
        Declaration r2=Declaration.create(OP("r2"));
        Axiom negOPa=NegativeObjectPropertyAssertion.create(OP("r2"),NI("a2"),NI("b2"),Annotation.create(AP("rdfs:label"), TL("rst2anno")));
        Declaration a3=Declaration.create(NI("a3"));
        Declaration b3=Declaration.create(NI("b3"));
        Declaration r3=Declaration.create(OP("r3"));
        Axiom negOPaa=NegativeObjectPropertyAssertion.create(OP("r3"),NI("a3"),NI("b3"),Annotation.create(AP("rdfs:comment"), TL("rst3Aanno"), Annotation.create(AP("rdfs:label"), TL("rs3annoAnno"))));
        String s=a1.toTurtleString()+b1.toTurtleString()+r1.toTurtleString()
            +a2.toTurtleString()+b2.toTurtleString()+r2.toTurtleString()
            +a3.toTurtleString()+b3.toTurtleString()+r3.toTurtleString()
            +negOP.toTurtleString()+negOPa.toTurtleString()+negOPaa.toTurtleString();
        OWLBGPParser parser=new OWLBGPParser(new StringReader(s));
        TripleConsumer consumer=parser.handler;
        parser.parse();
        assertTrue(consumer.getAxioms().remove(negOP));
        assertTrue(consumer.getAxioms().remove(negOPa));
        assertTrue(consumer.getAxioms().remove(negOPaa));
        assertTrue(consumer.getAxioms().remove(a1));
        assertTrue(consumer.getAxioms().remove(b1));
        assertTrue(consumer.getAxioms().remove(r1));
        assertTrue(consumer.getAxioms().remove(a2));
        assertTrue(consumer.getAxioms().remove(b2));
        assertTrue(consumer.getAxioms().remove(r2));
        assertTrue(consumer.getAxioms().remove(a3));
        assertTrue(consumer.getAxioms().remove(b3));
        assertTrue(consumer.getAxioms().remove(r3));
        assertTrue(consumer.getAxioms().isEmpty());
        assertNoTriplesLeft(consumer);
    }
    public void testDifferentFrom() throws Exception {
        Declaration a1=Declaration.create(NI("a1"));
        Declaration b1=Declaration.create(NI("b1"));
        Axiom diffInds=DifferentIndividuals.create(NI("a1"),NI("b1"));
        Declaration a2=Declaration.create(NI("a2"));
        Declaration b2=Declaration.create(NI("b2"));
        Set<Individual> inds=new HashSet<Individual>();
        inds.add(NI("a2"));
        inds.add(NI("b2"));
        Axiom diffIndsa=DifferentIndividuals.create(inds, Annotation.create(AP("rdfs:label"), TL("rst2anno")));
        Declaration a3=Declaration.create(NI("a3"));
        Declaration b3=Declaration.create(NI("b3"));
        Set<Individual> inds2=new HashSet<Individual>();
        inds2.add(NI("a3"));
        inds2.add(NI("b3"));
        Axiom diffIndsaa=DifferentIndividuals.create(inds2, Annotation.create(AP("rdfs:comment"), TL("rst3Aanno"), Annotation.create(AP("rdfs:label"), TL("rs3annoAnno"))));
        String s=a1.toTurtleString()+b1.toTurtleString()
            +a2.toTurtleString()+b2.toTurtleString()
            +a3.toTurtleString()+b3.toTurtleString()
            +diffInds.toTurtleString()+diffIndsa.toTurtleString()+diffIndsaa.toTurtleString();
        OWLBGPParser parser=new OWLBGPParser(new StringReader(s));
        TripleConsumer consumer=parser.handler;
        parser.parse();
        assertTrue(consumer.getAxioms().remove(diffInds));
        assertTrue(consumer.getAxioms().remove(diffIndsa));
        assertTrue(consumer.getAxioms().remove(diffIndsaa));
        assertTrue(consumer.getAxioms().remove(a1));
        assertTrue(consumer.getAxioms().remove(b1));
        assertTrue(consumer.getAxioms().remove(a2));
        assertTrue(consumer.getAxioms().remove(b2));
        assertTrue(consumer.getAxioms().remove(a3));
        assertTrue(consumer.getAxioms().remove(b3));
        assertTrue(consumer.getAxioms().isEmpty());
        assertNoTriplesLeft(consumer);
    }
    public void testAllDifferent() throws Exception {
        Declaration a1=Declaration.create(NI("a1"));
        Declaration b1=Declaration.create(NI("b1"));
        Declaration c1=Declaration.create(NI("c1"));
        Axiom diffInds=DifferentIndividuals.create(NI("a1"),NI("b1"),NI("c1"));
        Declaration a2=Declaration.create(NI("a2"));
        Declaration b2=Declaration.create(NI("b2"));
        Declaration c2=Declaration.create(NI("c2"));
        Set<Individual> inds=new HashSet<Individual>();
        inds.add(NI("a2"));
        inds.add(NI("b2"));
        inds.add(NI("c2"));
        Axiom diffIndsa=DifferentIndividuals.create(inds, Annotation.create(AP("rdfs:label"), TL("rst2anno")));
        Declaration a3=Declaration.create(NI("a3"));
        Declaration b3=Declaration.create(NI("b3"));
        Declaration c3=Declaration.create(NI("c3"));
        Set<Individual> inds2=new HashSet<Individual>();
        inds2.add(NI("a3"));
        inds2.add(NI("b3"));
        inds2.add(NI("c3"));
        Axiom diffIndsaa=DifferentIndividuals.create(inds2, Annotation.create(AP("rdfs:comment"), TL("rst3Aanno"), Annotation.create(AP("rdfs:label"), TL("rs3annoAnno"))));
        String s=a1.toTurtleString()+b1.toTurtleString()+c1.toTurtleString()
            +a2.toTurtleString()+b2.toTurtleString()+c2.toTurtleString()
            +a3.toTurtleString()+b3.toTurtleString()+c3.toTurtleString()
            +diffInds.toTurtleString()+diffIndsa.toTurtleString()+diffIndsaa.toTurtleString();
        OWLBGPParser parser=new OWLBGPParser(new StringReader(s));
        TripleConsumer consumer=parser.handler;
        parser.parse();
        assertTrue(consumer.getAxioms().remove(diffInds));
        assertTrue(consumer.getAxioms().remove(diffIndsa));
        assertTrue(consumer.getAxioms().remove(diffIndsaa));
        assertTrue(consumer.getAxioms().remove(a1));
        assertTrue(consumer.getAxioms().remove(b1));
        assertTrue(consumer.getAxioms().remove(c1));
        assertTrue(consumer.getAxioms().remove(a2));
        assertTrue(consumer.getAxioms().remove(b2));
        assertTrue(consumer.getAxioms().remove(c2));
        assertTrue(consumer.getAxioms().remove(a3));
        assertTrue(consumer.getAxioms().remove(b3));
        assertTrue(consumer.getAxioms().remove(c3));
        assertTrue(consumer.getAxioms().isEmpty());
        assertNoTriplesLeft(consumer);
    }
    public void testAllDisjointDataProperties() throws Exception {
        Declaration r1=Declaration.create(DP("r1"));
        Declaration s1=Declaration.create(DP("s1"));
        Declaration t1=Declaration.create(DP("t1"));
        Axiom djop=DisjointDataProperties.create(DP("r1"),DP("s1"),DP("t1"));
        Declaration r2=Declaration.create(DP("r2"));
        Declaration s2=Declaration.create(DP("s2"));
        Declaration t2=Declaration.create(DP("t2"));
        Set<DataPropertyExpression> dpes=new HashSet<DataPropertyExpression>();
        dpes.add(DP("r2"));
        dpes.add(DP("s2"));
        dpes.add(DP("t2"));
        Axiom djopa=DisjointDataProperties.create(dpes, Annotation.create(AP("rdfs:label"), TL("rst2anno")));
        Declaration r3=Declaration.create(DP("r3"));
        Declaration s3=Declaration.create(DP("s3"));
        Declaration t3=Declaration.create(DP("t3"));
        Set<DataPropertyExpression> dpes2=new HashSet<DataPropertyExpression>();
        dpes2.add(DP("r3"));
        dpes2.add(DP("s3"));
        dpes2.add(DP("t3"));
        Axiom djopaa=DisjointDataProperties.create(dpes2, Annotation.create(AP("rdfs:comment"), TL("rst3Aanno"), Annotation.create(AP("rdfs:label"), TL("rs3annoAnno"))));
        String s=r1.toTurtleString()+s1.toTurtleString()+t1.toTurtleString()
            +r2.toTurtleString()+s2.toTurtleString()+t2.toTurtleString()
            +r3.toTurtleString()+s3.toTurtleString()+t3.toTurtleString()
            +djop.toTurtleString()+djopa.toTurtleString()+djopaa.toTurtleString();
        OWLBGPParser parser=new OWLBGPParser(new StringReader(s));
        TripleConsumer consumer=parser.handler;
        parser.parse();
        assertTrue(consumer.getAxioms().remove(djop));
        assertTrue(consumer.getAxioms().remove(djopa));
        assertTrue(consumer.getAxioms().remove(djopaa));
        assertTrue(consumer.getAxioms().remove(r1));
        assertTrue(consumer.getAxioms().remove(s1));
        assertTrue(consumer.getAxioms().remove(t1));
        assertTrue(consumer.getAxioms().remove(r2));
        assertTrue(consumer.getAxioms().remove(s2));
        assertTrue(consumer.getAxioms().remove(t2));
        assertTrue(consumer.getAxioms().remove(r3));
        assertTrue(consumer.getAxioms().remove(s3));
        assertTrue(consumer.getAxioms().remove(t3));
        assertTrue(consumer.getAxioms().isEmpty());
        assertNoTriplesLeft(consumer);
    }
    public void testAllDisjointObjectProperties() throws Exception {
        Declaration r1=Declaration.create(OP("r1"));
        Declaration s1=Declaration.create(OP("s1"));
        Declaration t1=Declaration.create(OP("t1"));
        Axiom djop=DisjointObjectProperties.create(OP("r1"),OP("s1"),OP("t1"));
        Declaration r2=Declaration.create(OP("r2"));
        Declaration s2=Declaration.create(OP("s2"));
        Declaration t2=Declaration.create(OP("t2"));
        Set<ObjectPropertyExpression> opes=new HashSet<ObjectPropertyExpression>();
        opes.add(OP("r2"));
        opes.add(OP("s2"));
        opes.add(OP("t2"));
        Axiom djopa=DisjointObjectProperties.create(opes, Annotation.create(AP("rdfs:label"), TL("rst2anno")));
        Declaration r3=Declaration.create(OP("r3"));
        Declaration s3=Declaration.create(OP("s3"));
        Declaration t3=Declaration.create(OP("t3"));
        Set<ObjectPropertyExpression> opes2=new HashSet<ObjectPropertyExpression>();
        opes2.add(OP("r3"));
        opes2.add(OP("s3"));
        opes2.add(OP("t3"));
        Axiom djopaa=DisjointObjectProperties.create(opes2, Annotation.create(AP("rdfs:comment"), TL("rst3Aanno"), Annotation.create(AP("rdfs:label"), TL("rs3annoAnno"))));
        String s=r1.toTurtleString()+s1.toTurtleString()+t1.toTurtleString()
            +r2.toTurtleString()+s2.toTurtleString()+t2.toTurtleString()
            +r3.toTurtleString()+s3.toTurtleString()+t3.toTurtleString()
            +djop.toTurtleString()+djopa.toTurtleString()+djopaa.toTurtleString();
        OWLBGPParser parser=new OWLBGPParser(new StringReader(s));
        TripleConsumer consumer=parser.handler;
        parser.parse();
        assertTrue(consumer.getAxioms().remove(djop));
        assertTrue(consumer.getAxioms().remove(djopa));
        assertTrue(consumer.getAxioms().remove(djopaa));
        assertTrue(consumer.getAxioms().remove(r1));
        assertTrue(consumer.getAxioms().remove(s1));
        assertTrue(consumer.getAxioms().remove(t1));
        assertTrue(consumer.getAxioms().remove(r2));
        assertTrue(consumer.getAxioms().remove(s2));
        assertTrue(consumer.getAxioms().remove(t2));
        assertTrue(consumer.getAxioms().remove(r3));
        assertTrue(consumer.getAxioms().remove(s3));
        assertTrue(consumer.getAxioms().remove(t3));
        assertTrue(consumer.getAxioms().isEmpty());
        assertNoTriplesLeft(consumer);
    }
    public void testDisjointWithClasses() throws Exception {
        Declaration c=Declaration.create(C("C"));
        Declaration d=Declaration.create(C("D"));
        Axiom dc=DisjointClasses.create(C("C"),C("D"));
        Declaration e=Declaration.create(C("E"));
        Declaration f=Declaration.create(C("F"));
        Set<ClassExpression> ces=new HashSet<ClassExpression>();
        ces.add(C("E"));
        ces.add(C("F"));
        Axiom dca=DisjointClasses.create(ces, Annotation.create(AP("rdfs:label"), TL("EFanno")));
        Declaration g=Declaration.create(C("G"));
        Declaration h=Declaration.create(C("H"));
        ces=new HashSet<ClassExpression>();
        ces.add(C("G"));
        ces.add(C("H"));
        Axiom dcaa=DisjointClasses.create(ces, Annotation.create(AP("rdfs:comment"), TL("GHAanno"), Annotation.create(AP("rdfs:label"), TL("GHannoAnno"))));
        String s=c.toTurtleString()+d.toTurtleString()+e.toTurtleString()+f.toTurtleString()+g.toTurtleString()+h.toTurtleString()+dc.toTurtleString()+dca.toTurtleString()+dcaa.toTurtleString();
        OWLBGPParser parser=new OWLBGPParser(new StringReader(s));
        TripleConsumer consumer=parser.handler;
        parser.parse();
        assertTrue(consumer.getAxioms().remove(dc));
        assertTrue(consumer.getAxioms().remove(dca));
        assertTrue(consumer.getAxioms().remove(dcaa));
        assertTrue(consumer.getAxioms().remove(c));
        assertTrue(consumer.getAxioms().remove(d));
        assertTrue(consumer.getAxioms().remove(e));
        assertTrue(consumer.getAxioms().remove(f));
        assertTrue(consumer.getAxioms().remove(g));
        assertTrue(consumer.getAxioms().remove(h));
        assertTrue(consumer.getAxioms().isEmpty());
        assertNoTriplesLeft(consumer);
    }
    public void testAllDisjointClasses() throws Exception {
        Declaration c1=Declaration.create(C("C1"));
        Declaration d1=Declaration.create(C("D1"));
        Declaration e1=Declaration.create(C("E1"));
        Axiom dc=DisjointClasses.create(C("C1"),C("D1"),C("E1"));
        Declaration c2=Declaration.create(C("C2"));
        Declaration d2=Declaration.create(C("D2"));
        Declaration e2=Declaration.create(C("E2"));
        Set<ClassExpression> ces=new HashSet<ClassExpression>();
        ces.add(C("C2"));
        ces.add(C("D2"));
        ces.add(C("E2"));
        Axiom dca=DisjointClasses.create(ces, Annotation.create(AP("rdfs:label"), TL("CDE2anno")));
        Declaration c3=Declaration.create(C("C3"));
        Declaration d3=Declaration.create(C("D3"));
        Declaration e3=Declaration.create(C("E3"));
        Set<ClassExpression> ces2=new HashSet<ClassExpression>();
        ces2.add(C("C3"));
        ces2.add(C("D3"));
        ces2.add(C("E3"));
        Axiom dcaa=DisjointClasses.create(ces2, Annotation.create(AP("rdfs:comment"), TL("CDE3Aanno"), Annotation.create(AP("rdfs:label"), TL("CDE3annoAnno"))));
        String s=c1.toTurtleString()+d1.toTurtleString()+e1.toTurtleString()
            +c2.toTurtleString()+d2.toTurtleString()+e2.toTurtleString()
            +c3.toTurtleString()+d3.toTurtleString()+e3.toTurtleString()
            +dc.toTurtleString()+dca.toTurtleString()+dcaa.toTurtleString();
        OWLBGPParser parser=new OWLBGPParser(new StringReader(s));
        TripleConsumer consumer=parser.handler;
        parser.parse();
        assertTrue(consumer.getAxioms().remove(dc));
        assertTrue(consumer.getAxioms().remove(dca));
        assertTrue(consumer.getAxioms().remove(dcaa));
        assertTrue(consumer.getAxioms().remove(c1));
        assertTrue(consumer.getAxioms().remove(d1));
        assertTrue(consumer.getAxioms().remove(e1));
        assertTrue(consumer.getAxioms().remove(c2));
        assertTrue(consumer.getAxioms().remove(d2));
        assertTrue(consumer.getAxioms().remove(e2));
        assertTrue(consumer.getAxioms().remove(c3));
        assertTrue(consumer.getAxioms().remove(d3));
        assertTrue(consumer.getAxioms().remove(e3));
        assertTrue(consumer.getAxioms().isEmpty());
        assertNoTriplesLeft(consumer);
    }
    public void testDeclarationsWithAnnotationAnnotations() throws Exception {
        Annotation caa=Annotation.create(AP("ap"), TL("caa"));
        Annotation ca=Annotation.create(AP("ap"), TL("ca"),caa);
        Annotation opaa=Annotation.create(AP("ap"), TL("opaa"));
        Annotation opa=Annotation.create(AP("ap"), TL("opa"),opaa);
        Annotation dpaa=Annotation.create(AP("ap"), TL("dpaa"));
        Annotation dpa=Annotation.create(AP("ap"), TL("dpa"),dpaa);
        Annotation apaa=Annotation.create(AP("ap"), TL("apaa"));
        Annotation apa=Annotation.create(AP("ap"), TL("apa"),apaa);
        Annotation niaa=Annotation.create(AP("ap"), TL("niaa"));
        Annotation nia=Annotation.create(AP("ap"), TL("nia"),niaa);
        Annotation dtaa=Annotation.create(AP("ap"), TL("dtaa"));
        Annotation dta=Annotation.create(AP("ap"), TL("dta"),dtaa);
        Declaration c=Declaration.create(C("C"), ca);
        Declaration op=Declaration.create(OP("op"), opa);
        Declaration dp=Declaration.create(DP("dp"), dpa);
        Declaration ap=Declaration.create(AP("ap"), apa);
        Declaration ni=Declaration.create(NI("ni"), nia);
        Declaration dt=Declaration.create(DT("dt"), dta);
        String s=c.toTurtleString()+op.toTurtleString()+dp.toTurtleString()+ap.toTurtleString()+ni.toTurtleString()+dt.toTurtleString();
        OWLBGPParser parser=new OWLBGPParser(new StringReader(s));
        TripleConsumer consumer=parser.handler;
        parser.parse();
        assertTrue(consumer.getAxioms().remove(c));
        assertTrue(consumer.getAxioms().remove(op));
        assertTrue(consumer.getAxioms().remove(dp));
        assertTrue(consumer.getAxioms().remove(ap));
        assertTrue(consumer.getAxioms().remove(ni));
        assertTrue(consumer.getAxioms().remove(dt));
        assertTrue(consumer.getAxioms().isEmpty());
        assertNoTriplesLeft(consumer);
    }
    public void testDeclarationsWithAnnotations() throws Exception {
        Annotation ca=Annotation.create(AP("ap"), TL("ca"));
        Annotation opa=Annotation.create(AP("ap"), TL("opa"));
        Annotation dpa=Annotation.create(AP("ap"), TL("dpa"));
        Annotation apa=Annotation.create(AP("ap"), TL("apa"));
        Annotation nia=Annotation.create(AP("ap"), TL("nia"));
        Annotation dta=Annotation.create(AP("ap"), TL("dta"));
        Declaration c=Declaration.create(C("C"), ca);
        Declaration op=Declaration.create(OP("op"), opa);
        Declaration dp=Declaration.create(DP("dp"), dpa);
        Declaration ap=Declaration.create(AP("ap"), apa);
        Declaration ni=Declaration.create(NI("ni"), nia);
        Declaration dt=Declaration.create(DT("dt"), dta);
        String s=c.toTurtleString()+op.toTurtleString()+dp.toTurtleString()+ap.toTurtleString()+ni.toTurtleString()+dt.toTurtleString();
        OWLBGPParser parser=new OWLBGPParser(new StringReader(s));
        TripleConsumer consumer=parser.handler;
        parser.parse();
        assertTrue(consumer.getAxioms().remove(c));
        assertTrue(consumer.getAxioms().remove(op));
        assertTrue(consumer.getAxioms().remove(dp));
        assertTrue(consumer.getAxioms().remove(ap));
        assertTrue(consumer.getAxioms().remove(ni));
        assertTrue(consumer.getAxioms().remove(dt));
        assertTrue(consumer.getAxioms().isEmpty());
        assertNoTriplesLeft(consumer);
    }
    public void testDeclarations() throws Exception {
        Declaration c=Declaration.create(C("C"));
        Declaration op=Declaration.create(OP("op"));
        Declaration dp=Declaration.create(DP("dp"));
        Declaration ap=Declaration.create(AP("ap"));
        Declaration ni=Declaration.create(NI("ni"));
        Declaration dt=Declaration.create(DT("dt"));
        String s=c.toTurtleString()+op.toTurtleString()+dp.toTurtleString()+ap.toTurtleString()+ni.toTurtleString()+dt.toTurtleString();
        OWLBGPParser parser=new OWLBGPParser(new StringReader(s));
        TripleConsumer consumer=parser.handler;
        parser.parse();
        assertTrue(consumer.getAxioms().remove(c));
        assertTrue(consumer.getAxioms().remove(op));
        assertTrue(consumer.getAxioms().remove(dp));
        assertTrue(consumer.getAxioms().remove(ap));
        assertTrue(consumer.getAxioms().remove(ni));
        assertTrue(consumer.getAxioms().remove(dt));
        assertTrue(consumer.getAxioms().isEmpty());
        assertNoTriplesLeft(consumer);
    }
}