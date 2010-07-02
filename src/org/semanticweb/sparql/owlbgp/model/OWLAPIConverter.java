package org.semanticweb.sparql.owlbgp.model;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLFacetRestriction;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.vocab.OWLFacet;

public class OWLAPIConverter implements ExtendedOWLObjectVisitorEx<OWLObject> {
    protected final OWLDataFactory m_dataFactory;
    
    public OWLAPIConverter(OWLDataFactory dataFactory) {
        m_dataFactory=dataFactory;
    }
    public OWLObject visit(Clazz aClass) {
        return m_dataFactory.getOWLClass(IRI.create(aClass.m_iri));
    }
    public OWLObject visit(ClassVariable classVariable) {
        if (classVariable.m_binding==null) throw new RuntimeException("Error: Can only convert to OWL API objects if all variables are bound, but variable "+classVariable.m_variable+" is unbound. ");
        return m_dataFactory.getOWLClass(IRI.create(classVariable.m_binding));
    }
    public OWLObject visit(ObjectIntersectionOf objectIntersectionOf) {
        Set<OWLClassExpression> classExpressions=new HashSet<OWLClassExpression>();
        for (ClassExpression classExpression : objectIntersectionOf.getClassExpressions())
            classExpressions.add((OWLClassExpression)classExpression.accept(this));
        return m_dataFactory.getOWLObjectIntersectionOf(classExpressions);
    }
    public OWLObject visit(ObjectUnionOf objectUnionOf) {
      Set<OWLClassExpression> classExpressions=new HashSet<OWLClassExpression>();
      for (ClassExpression classExpression : objectUnionOf.m_classExpressions)
          classExpressions.add((OWLClassExpression)classExpression.accept(this));
      return m_dataFactory.getOWLObjectUnionOf(classExpressions);
    }
    public OWLObject visit(ObjectComplementOf objectComplementOf) {
        return m_dataFactory.getOWLObjectComplementOf((OWLClassExpression)objectComplementOf.m_classExpression.accept(this));
    }
    public OWLObject visit(ObjectAllValuesFrom allValuesFrom) {
        return m_dataFactory.getOWLObjectAllValuesFrom((OWLObjectPropertyExpression)allValuesFrom.m_ope.accept(this),(OWLClassExpression)allValuesFrom.m_classExpression.accept(this));
    }
    public OWLObject visit(ObjectSomeValuesFrom objectSomeValuesFrom) {
        return m_dataFactory.getOWLObjectSomeValuesFrom((OWLObjectPropertyExpression)objectSomeValuesFrom.m_ope.accept(this),(OWLClassExpression)objectSomeValuesFrom.m_classExpression.accept(this));
    }
    public OWLObject visit(ObjectHasValue objectHasValue) {
        return m_dataFactory.getOWLObjectHasValue((OWLObjectPropertyExpression)objectHasValue.m_ope.accept(this),(OWLNamedIndividual)objectHasValue.m_individual.accept(this));
    }
    public OWLObject visit(ObjectMinCardinality objectMinCardinality) {
        return m_dataFactory.getOWLObjectMinCardinality(objectMinCardinality.m_cardinality,(OWLObjectPropertyExpression)objectMinCardinality.m_ope.accept(this),(OWLClassExpression)objectMinCardinality.m_classExpression.accept(this));
    }
    public OWLObject visit(ObjectExactCardinality objectExactCardinality) {
        return m_dataFactory.getOWLObjectExactCardinality(objectExactCardinality.m_cardinality,(OWLObjectPropertyExpression)objectExactCardinality.m_ope.accept(this),(OWLClassExpression)objectExactCardinality.m_classExpression.accept(this));
    }
    public OWLObject visit(ObjectMaxCardinality objectMaxCardinality) {
        return m_dataFactory.getOWLObjectMaxCardinality(objectMaxCardinality.m_cardinality,(OWLObjectPropertyExpression)objectMaxCardinality.m_ope.accept(this),(OWLClassExpression)objectMaxCardinality.m_classExpression.accept(this));
    }
    public OWLObject visit(ObjectHasSelf objectHasSelf) {
        return m_dataFactory.getOWLObjectHasSelf((OWLObjectPropertyExpression)objectHasSelf.m_ope.accept(this));
    }
    public OWLObject visit(ObjectOneOf objectOneOf) {
        Set<OWLIndividual> individuals=new HashSet<OWLIndividual>();
        for (Individual individual : objectOneOf.m_enumeration)
            individuals.add((OWLIndividual)individual.accept(this));
      return m_dataFactory.getOWLObjectOneOf(individuals);
    }
    public OWLObject visit(DataSomeValuesFrom dataSomeValuesFrom) {
        return m_dataFactory.getOWLDataSomeValuesFrom((OWLDataPropertyExpression)dataSomeValuesFrom.m_dpe.accept(this),(OWLDataRange)dataSomeValuesFrom.m_dataRange.accept(this));
    }
    public OWLObject visit(DataAllValuesFrom dataAllValuesFrom) {
        return m_dataFactory.getOWLDataAllValuesFrom((OWLDataPropertyExpression)dataAllValuesFrom.m_dpe.accept(this),(OWLDataRange)dataAllValuesFrom.m_dataRange.accept(this));
    }
    public OWLObject visit(DataHasValue dataHasValue) {
        return m_dataFactory.getOWLDataHasValue((OWLDataPropertyExpression)dataHasValue.m_dpe.accept(this),(OWLLiteral)dataHasValue.m_literal.accept(this));
    }
    public OWLObject visit(DataMinCardinality dataMinCardinality) {
        return m_dataFactory.getOWLDataMinCardinality(dataMinCardinality.m_cardinality,(OWLDataPropertyExpression)dataMinCardinality.m_dpe.accept(this),(OWLDataRange)dataMinCardinality.m_dataRange.accept(this));
    }
    public OWLObject visit(DataExactCardinality dataExactCardinality) {
        return m_dataFactory.getOWLDataMaxCardinality(dataExactCardinality.m_cardinality,(OWLDataPropertyExpression)dataExactCardinality.m_dpe.accept(this),(OWLDataRange)dataExactCardinality.m_dataRange.accept(this));
    }
    public OWLObject visit(DataMaxCardinality dataMaxCardinality) {
        return m_dataFactory.getOWLDataMaxCardinality(dataMaxCardinality.m_cardinality,(OWLDataPropertyExpression)dataMaxCardinality.m_dpe.accept(this),(OWLDataRange)dataMaxCardinality.m_dataRange.accept(this));
    }
    public OWLObject visit(ObjectProperty objectProperty) {
        return m_dataFactory.getOWLObjectProperty(IRI.create(objectProperty.m_iri));
    }
    public OWLObject visit(InverseObjectProperty inverseObjectProperty) {
        return m_dataFactory.getOWLObjectInverseOf((OWLObjectPropertyExpression)inverseObjectProperty.m_ope.accept(this));
    }
    public OWLObject visit(ObjectPropertyVariable objectPropertyVariable) {
      if (objectPropertyVariable.m_binding==null) throw new RuntimeException("Error: Can only convert to OWL API objects if all variables are bound, but variable "+objectPropertyVariable.m_variable+" is unbound. ");
      return m_dataFactory.getOWLObjectProperty(IRI.create(objectPropertyVariable.m_binding));
    }
    public OWLObject visit(DataProperty dataProperty) {
        return m_dataFactory.getOWLDataProperty(IRI.create(dataProperty.m_iri));
    }
    public OWLObject visit(DataPropertyVariable dataPropertyVariable) {
        if (dataPropertyVariable.m_binding==null) throw new RuntimeException("Error: Can only convert to OWL API objects if all variables are bound, but variable "+dataPropertyVariable.m_variable+" is unbound. ");
        return m_dataFactory.getOWLObjectProperty(IRI.create(dataPropertyVariable.m_binding));
    }
    public OWLObject visit(Literal literal) {
        if (literal.m_langTag!=null) return m_dataFactory.getOWLStringLiteral(literal.m_lexicalForm,literal.m_langTag);
        else return m_dataFactory.getOWLTypedLiteral(literal.m_lexicalForm,(OWLDatatype)literal.m_dataDatatype.accept(this));
    }
    public OWLObject visit(LiteralVariable literalVariable) {
        if (literalVariable.m_literalBinding==null) throw new RuntimeException("Error: Can only convert to OWL API objects if all variables are bound, but variable "+literalVariable.m_variable+" is unbound. ");
        if (literalVariable.m_literalBinding.getLangTag()!=null) return m_dataFactory.getOWLStringLiteral(literalVariable.m_literalBinding.getLexicalForm(),literalVariable.m_literalBinding.getLangTag());
        else return m_dataFactory.getOWLTypedLiteral(literalVariable.m_literalBinding.getLexicalForm(),(OWLDatatype)literalVariable.m_literalBinding.getDatatype().accept(this));
    }
    public OWLObject visit(NamedIndividual namedIndividual) {
        return m_dataFactory.getOWLNamedIndividual(IRI.create(namedIndividual.m_iri));
    }
    public OWLObject visit(AnonymousIndividual anonymousIndividual) {
        return m_dataFactory.getOWLAnonymousIndividual(anonymousIndividual.m_nodeID);
    }
    public OWLObject visit(IndividualVariable individualVariable) {
        if (individualVariable.m_binding==null) throw new RuntimeException("Error: Can only convert to OWL API objects if all variables are bound, but variable "+individualVariable.m_variable+" is unbound. ");
        if (individualVariable.m_binding.startsWith("_:"))
            return m_dataFactory.getOWLAnonymousIndividual(individualVariable.m_binding);
        else 
            return m_dataFactory.getOWLNamedIndividual(IRI.create(individualVariable.m_binding));
    }
    public OWLObject visit(Datatype datatype) {
        return m_dataFactory.getOWLDatatype(IRI.create(datatype.m_iri));
    }
    public OWLObject visit(DatatypeVariable datatypeVariable) {
        if (datatypeVariable.m_binding==null) throw new RuntimeException("Error: Can only convert to OWL API objects if all variables are bound, but variable "+datatypeVariable.m_variable+" is unbound. ");
        return m_dataFactory.getOWLDatatype(IRI.create(datatypeVariable.m_binding));
    }
    public OWLObject visit(DatatypeRestriction datatypeRestriction) {
        Set<OWLFacetRestriction> facetRestrictions=new HashSet<OWLFacetRestriction>();
        for (FacetRestriction facetRestriction : datatypeRestriction.m_facetRestrictions) {
            facetRestrictions.add((OWLFacetRestriction)facetRestriction.m_literal.accept(this));
        }
        return m_dataFactory.getOWLDatatypeRestriction((OWLDatatype)datatypeRestriction.m_datatype.accept(this),facetRestrictions);
    }
    public OWLObject visit(FacetRestriction facetRestriction) {
        return m_dataFactory.getOWLFacetRestriction(OWLFacet.getFacet(IRI.create(facetRestriction.m_facet.m_iri)), (OWLLiteral)facetRestriction.m_literal.accept(this));
    }
    public OWLObject visit(DataComplementOf dataComplementOf) {
        return m_dataFactory.getOWLDataComplementOf((OWLDataRange)dataComplementOf.m_dataRange.accept(this));
    }
    public OWLObject visit(DataIntersectionOf dataIntersectionOf) {
        Set<OWLDataRange> dataRanges=new HashSet<OWLDataRange>();
        for (DataRange dataRange : dataIntersectionOf.m_dataRanges)
            dataRanges.add((OWLDataRange)dataRange.accept(this));
        return m_dataFactory.getOWLDataIntersectionOf(dataRanges);
    }
    public OWLObject visit(DataUnionOf dataUnionOf) {
        Set<OWLDataRange> dataRanges=new HashSet<OWLDataRange>();
        for (DataRange dataRange : dataUnionOf.m_dataRanges)
            dataRanges.add((OWLDataRange)dataRange.accept(this));
        return m_dataFactory.getOWLDataUnionOf(dataRanges);
    }
    public OWLObject visit(DataOneOf dataOneOf) {
        Set<OWLLiteral> literals=new HashSet<OWLLiteral>();
        for (ILiteral literal : dataOneOf.m_enumeration)
            literals.add((OWLLiteral)literal.accept(this));
        return m_dataFactory.getOWLDataOneOf(literals);
    }
    public OWLObject visit(SubClassOf axiom) {
        return m_dataFactory.getOWLSubClassOfAxiom((OWLClassExpression)axiom.m_subClass.accept(this),(OWLClassExpression)axiom.m_superClass.accept(this));
    }
    public OWLObject visit(EquivalentClasses axiom) {
        Set<OWLClassExpression> classExpressions=new HashSet<OWLClassExpression>();
        for (ClassExpression classExpression : axiom.getClassExpressions())
            classExpressions.add((OWLClassExpression)classExpression.accept(this));
        return m_dataFactory.getOWLEquivalentClassesAxiom(classExpressions);
    }
    public OWLObject visit(DisjointClasses axiom) {
        Set<OWLClassExpression> classExpressions=new HashSet<OWLClassExpression>();
        for (ClassExpression classExpression : axiom.getClassExpressions())
            classExpressions.add((OWLClassExpression)classExpression.accept(this));
        return m_dataFactory.getOWLDisjointClassesAxiom(classExpressions);
    }
    public OWLObject visit(DisjointUnion axiom) {
        Set<OWLClassExpression> classExpressions=new HashSet<OWLClassExpression>();
        for (ClassExpression classExpression : axiom.getClassExpressions())
            classExpressions.add((OWLClassExpression)classExpression.accept(this));
        return m_dataFactory.getOWLDisjointUnionAxiom((OWLClass)axiom.m_class.accept(this), classExpressions);
    }
    
    public OWLObject visit(SubObjectPropertyOf axiom) {
        return m_dataFactory.getOWLSubObjectPropertyOfAxiom((OWLObjectPropertyExpression)axiom.m_subope.accept(this),(OWLObjectPropertyExpression)axiom.m_superope.accept(this));
    }
    public OWLObject visit(EquivalentObjectProperties axiom) {
        Set<OWLObjectPropertyExpression> opes=new HashSet<OWLObjectPropertyExpression>();
        for (ObjectPropertyExpression ope : axiom.getObjectPropertyExpressions())
            opes.add((OWLObjectPropertyExpression)ope.accept(this));
        return m_dataFactory.getOWLEquivalentObjectPropertiesAxiom(opes);
    }
    public OWLObject visit(DisjointObjectProperties axiom) {
        Set<OWLObjectPropertyExpression> opes=new HashSet<OWLObjectPropertyExpression>();
        for (ObjectPropertyExpression ope : axiom.getObjectPropertyExpressions())
            opes.add((OWLObjectPropertyExpression)ope.accept(this));
        return m_dataFactory.getOWLDisjointObjectPropertiesAxiom(opes);
    }
    public OWLObject visit(InverseObjectProperties axiom) {
        return m_dataFactory.getOWLInverseObjectPropertiesAxiom((OWLObjectPropertyExpression)axiom.m_ope.accept(this),(OWLObjectPropertyExpression)axiom.m_inverseOpe.accept(this));
    }
    public OWLObject visit(ObjectPropertyDomain axiom) {
        return m_dataFactory.getOWLObjectPropertyDomainAxiom((OWLObjectPropertyExpression)axiom.m_ope.accept(this),(OWLClassExpression)axiom.m_classExpression.accept(this));
    }
    public OWLObject visit(ObjectPropertyRange axiom) {
        return m_dataFactory.getOWLObjectPropertyDomainAxiom((OWLObjectPropertyExpression)axiom.m_ope.accept(this),(OWLClassExpression)axiom.m_classExpression.accept(this));
    }
    public OWLObject visit(FunctionalObjectProperty axiom) {
        return m_dataFactory.getOWLFunctionalObjectPropertyAxiom((OWLObjectPropertyExpression)axiom.m_ope.accept(this));
    }
    public OWLObject visit(InverseFunctionalObjectProperty axiom) {
        return m_dataFactory.getOWLInverseFunctionalObjectPropertyAxiom((OWLObjectPropertyExpression)axiom.m_ope.accept(this));
    }
    public OWLObject visit(ReflexiveObjectProperty axiom) {
        return m_dataFactory.getOWLReflexiveObjectPropertyAxiom((OWLObjectPropertyExpression)axiom.m_ope.accept(this));
    }
    public OWLObject visit(IrreflexiveObjectProperty axiom) {
        return m_dataFactory.getOWLIrreflexiveObjectPropertyAxiom((OWLObjectPropertyExpression)axiom.m_ope.accept(this));
    }
    public OWLObject visit(SymmetricObjectProperty axiom) {
        return m_dataFactory.getOWLSymmetricObjectPropertyAxiom((OWLObjectPropertyExpression)axiom.m_ope.accept(this));
    }
    public OWLObject visit(AsymmetricObjectProperty axiom) {
        return m_dataFactory.getOWLAsymmetricObjectPropertyAxiom((OWLObjectPropertyExpression)axiom.m_ope.accept(this));
    }
    public OWLObject visit(TransitiveObjectProperty axiom) {
        return m_dataFactory.getOWLTransitiveObjectPropertyAxiom((OWLObjectPropertyExpression)axiom.m_ope.accept(this));
    }
    
    public OWLObject visit(SubDataPropertyOf axiom) {
        return m_dataFactory.getOWLSubDataPropertyOfAxiom((OWLDataPropertyExpression)axiom.m_subdpe.accept(this),(OWLDataPropertyExpression)axiom.m_superdpe.accept(this));
    }
    public OWLObject visit(EquivalentDataProperties axiom) {
        Set<OWLDataPropertyExpression> dpes=new HashSet<OWLDataPropertyExpression>();
        for (DataPropertyExpression dpe : axiom.getDataPropertyExpressions())
            dpes.add((OWLDataPropertyExpression)dpe.accept(this));
        return m_dataFactory.getOWLEquivalentDataPropertiesAxiom(dpes);
    }
    public OWLObject visit(DisjointDataProperties axiom) {
        Set<OWLDataPropertyExpression> dpes=new HashSet<OWLDataPropertyExpression>();
        for (DataPropertyExpression dpe : axiom.getDataPropertyExpressions())
            dpes.add((OWLDataPropertyExpression)dpe.accept(this));
        return m_dataFactory.getOWLDisjointDataPropertiesAxiom(dpes);
    }
    public OWLObject visit(DataPropertyDomain axiom) {
        return m_dataFactory.getOWLDataPropertyDomainAxiom((OWLDataPropertyExpression)axiom.m_dpe.accept(this),(OWLClassExpression)axiom.m_classExpression.accept(this));
    }
    public OWLObject visit(DataPropertyRange axiom) {
        return m_dataFactory.getOWLDataPropertyRangeAxiom((OWLDataPropertyExpression)axiom.m_dpe.accept(this),(OWLDataRange)axiom.m_dataRange.accept(this));
    }
    public OWLObject visit(FunctionalDataProperty axiom) {
        return m_dataFactory.getOWLFunctionalDataPropertyAxiom((OWLDataPropertyExpression)axiom.m_dpe.accept(this));
    }
    
    public OWLObject visit(DatatypeDefinition axiom) {
        return m_dataFactory.getOWLDatatypeDefinitionAxiom((OWLDatatype)axiom.m_datatype.accept(this),(OWLDataRange)axiom.m_dataRange.accept(this));
    }
    
    public OWLObject visit(HasKey axiom) {
        Set<OWLObjectPropertyExpression> opes=new HashSet<OWLObjectPropertyExpression>();
        for (ObjectPropertyExpression ope : axiom.m_objectPropertyExpressions) {
            opes.add((OWLObjectPropertyExpression)ope.accept(this));
        }
        Set<OWLDataPropertyExpression> dpes=new HashSet<OWLDataPropertyExpression>();
        for (DataPropertyExpression dpe : axiom.m_dataPropertyExpressions) {
            dpes.add((OWLDataPropertyExpression)dpe.accept(this));
        }
        if (opes.size()>0)
            return m_dataFactory.getOWLHasKeyAxiom((OWLClassExpression)axiom.m_classExpression.accept(this),opes);
        else 
            return m_dataFactory.getOWLHasKeyAxiom((OWLClassExpression)axiom.m_classExpression.accept(this),dpes);
    }
}