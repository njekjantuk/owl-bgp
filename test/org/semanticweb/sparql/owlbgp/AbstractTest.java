package org.semanticweb.sparql.owlbgp;

import junit.framework.TestCase;

import org.semanticweb.sparql.owlbgp.model.IRI;
import org.semanticweb.sparql.owlbgp.model.Prefixes;
import org.semanticweb.sparql.owlbgp.model.UntypedVariable;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassVariable;
import org.semanticweb.sparql.owlbgp.model.classexpressions.Clazz;
import org.semanticweb.sparql.owlbgp.model.dataranges.Datatype;
import org.semanticweb.sparql.owlbgp.model.dataranges.DatatypeVariable;
import org.semanticweb.sparql.owlbgp.model.individuals.AnonymousIndividual;
import org.semanticweb.sparql.owlbgp.model.individuals.IndividualVariable;
import org.semanticweb.sparql.owlbgp.model.individuals.NamedIndividual;
import org.semanticweb.sparql.owlbgp.model.literals.TypedLiteral;
import org.semanticweb.sparql.owlbgp.model.properties.AnnotationProperty;
import org.semanticweb.sparql.owlbgp.model.properties.AnnotationPropertyVariable;
import org.semanticweb.sparql.owlbgp.model.properties.DataProperty;
import org.semanticweb.sparql.owlbgp.model.properties.DataPropertyVariable;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectInverseOf;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectProperty;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyVariable;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;

public class AbstractTest extends TestCase {
    public static final String LB=System.getProperty("line.separator");
    public static final String NS="http://example.org/";
    
    protected void assertNoTriplesLeft(TripleConsumer consumer) {
//        assertTrue(consumer.allTriplesConsumed());
    }
    
    public static UntypedVariable V(String variableName) {
        return UntypedVariable.create(variableName);
    }
    public static IRI IRI(String iri) {
        if (iri.startsWith("xsd:")) iri=Prefixes.s_semanticWebPrefixes.get("xsd")+iri.substring(4);
        else if (iri.startsWith("rdf:")) iri=Prefixes.s_semanticWebPrefixes.get("rdf")+iri.substring(4);
        else if (iri.startsWith("rdfs:")) iri=Prefixes.s_semanticWebPrefixes.get("rdfs")+iri.substring(5);
        else if (!iri.startsWith("<") && !iri.startsWith("http")) iri=NS+iri;
        return IRI.create(iri);
    }
    public static Clazz C(String className) {
        if (!className.startsWith("<") && !className.startsWith("http")) className=NS+className;
        return Clazz.create(className);
    }
    public static Clazz C(IRI classIri) {
        return Clazz.create(classIri);
    }
    public static ClassVariable CV(String classVarName) {
        return ClassVariable.create(classVarName);
    }
    public static Datatype DT(String datatypeUri) {
        if (datatypeUri.startsWith("xsd:")) datatypeUri=Prefixes.s_semanticWebPrefixes.get("xsd")+datatypeUri.substring(4);
        else if (datatypeUri.startsWith("rdf:")) datatypeUri=Prefixes.s_semanticWebPrefixes.get("rdf")+datatypeUri.substring(4);
        else if (datatypeUri.startsWith("rdfs:")) datatypeUri=Prefixes.s_semanticWebPrefixes.get("rdfs")+datatypeUri.substring(5);
        else if (!datatypeUri.startsWith("<") && !datatypeUri.startsWith("http")) datatypeUri=NS+datatypeUri;
        return Datatype.create(datatypeUri);
    }
    public static Datatype DT(IRI datatypeIri) {
        return Datatype.create(datatypeIri);
    }
    public static DatatypeVariable DTV(String datatypeVarName) {
        return DatatypeVariable.create(datatypeVarName);
    }
    public static DatatypeVariable DTV(UntypedVariable datatypeVar) {
        return DatatypeVariable.create(datatypeVar.toString());
    }
    public static ObjectProperty OP(String objectPropertyName) {
        if (!objectPropertyName.startsWith("<") && !objectPropertyName.startsWith("http")) objectPropertyName=NS+objectPropertyName;
        return ObjectProperty.create(objectPropertyName);
    }
    public static ObjectProperty OP(IRI objectPropertyIri) {
        return ObjectProperty.create(objectPropertyIri);
    }
    public static ObjectInverseOf IOP(String objectPropertyName) {
        if (!objectPropertyName.startsWith("<") && !objectPropertyName.startsWith("http")) objectPropertyName=NS+objectPropertyName;
        return ObjectInverseOf.create(OP(objectPropertyName));
    }
    public static ObjectInverseOf IOP(IRI objectPropertyIri) {
        return ObjectInverseOf.create(OP(objectPropertyIri));
    }
    public static ObjectPropertyVariable OPV(String objectPropertyVarName) {
        return ObjectPropertyVariable.create(objectPropertyVarName);
    }
    public static DataProperty DP(String dataPropertyName) {
        if (!dataPropertyName.startsWith("<") && !dataPropertyName.startsWith("http")) dataPropertyName=NS+dataPropertyName;
        return DataProperty.create(dataPropertyName);
    }
    public static DataProperty DP(IRI dataPropertyIri) {
        return DataProperty.create(dataPropertyIri);
    }
    public static DataPropertyVariable DPV(String dataPropertyVarName) {
        return DataPropertyVariable.create(dataPropertyVarName);
    }
    public static AnnotationProperty AP(String annotationPropertyName) {
        if (!annotationPropertyName.startsWith("<") && !annotationPropertyName.startsWith("http")) annotationPropertyName=NS+annotationPropertyName;
        return AnnotationProperty.create(annotationPropertyName);
    }
    public static AnnotationProperty AP(IRI annotationPropertyIri) {
        return AnnotationProperty.create(annotationPropertyIri);
    }
    public static AnnotationPropertyVariable APV(String annotationPropertyVarName) {
        return AnnotationPropertyVariable.create(annotationPropertyVarName);
    }
    public static NamedIndividual NI(String individualName) {
        if (!individualName.startsWith("<") && !individualName.startsWith("http")) individualName=NS+individualName;
        return NamedIndividual.create(individualName);
    }
    public static NamedIndividual NI(IRI individualIri) {
        return NamedIndividual.create(individualIri);
    }
    public static IndividualVariable IV(String individualVarName) {
        return IndividualVariable.create(individualVarName);
    }
    public static AnonymousIndividual AI(String label) {
        return AnonymousIndividual.create(label);
    }
    public static TypedLiteral TL(String lexicalForm) {
        return TL(lexicalForm,(String)null,(Datatype)null);
    }
    public static TypedLiteral TL(String lexicalForm, String langTag) {
        return TL(lexicalForm, langTag, (Datatype)null);
    }
    public static TypedLiteral TL(String lexicalForm, String langTag, String datatypeUri) {
        return TL(lexicalForm, langTag, DT(datatypeUri));
    }
    public static TypedLiteral TL(String lexicalForm, String langTag, IRI datatypeIri) {
        return TypedLiteral.create(lexicalForm, langTag, DT(datatypeIri));
    }
    public static TypedLiteral TL(String lexicalForm, String langTag, Datatype datatype) {
        return TypedLiteral.create(lexicalForm, langTag, datatype);
    }
}