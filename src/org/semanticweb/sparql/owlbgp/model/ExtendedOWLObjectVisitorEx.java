package org.semanticweb.sparql.owlbgp.model;

public interface ExtendedOWLObjectVisitorEx<O> {
    O visit(Clazz clazz);
    O visit(ClassVariable classVariable);
    O visit(ObjectIntersectionOf objectIntersectionOf);
    O visit(ObjectUnionOf objectUnionOf);
    O visit(ObjectComplementOf objectComplementOf);
    O visit(ObjectSomeValuesFrom objectSomeValuesFrom);
    O visit(ObjectAllValuesFrom objectAllValuesFrom);
    O visit(ObjectHasValue objectHasValue);
    O visit(ObjectMinCardinality objectMinCardinality);
    O visit(ObjectExactCardinality objectExactCardinality);
    O visit(ObjectMaxCardinality objectMaxCardinality);
    O visit(ObjectHasSelf objectHasSelf);
    O visit(ObjectOneOf objectOneOf);
    O visit(DataSomeValuesFrom dataSomeValuesFrom);
    O visit(DataAllValuesFrom dataAllValuesFrom);
    O visit(DataHasValue dataHasValue);
    O visit(DataMinCardinality dataMinCardinality );
    O visit(DataExactCardinality dataExactCardinality);
    O visit(DataMaxCardinality dataMaxCardinality);
    
    O visit(ObjectProperty objectProperty);
    O visit(ObjectInverseOf inverseObjectProperty);
    O visit(ObjectPropertyVariable objectPropertyVariable);
    O visit(DataProperty dataProperty);
    O visit(DataPropertyVariable dataPropertyVariable);
    O visit(AnnotationProperty annotationProperty);
    
    O visit(Literal literal);
    O visit(LiteralVariable literalVariable);
    
    O visit(NamedIndividual namedIndividual);
    O visit(AnonymousIndividual anonymousIndividual);
    O visit(IndividualVariable individualVariable);
    
    O visit(Datatype datatype);
    O visit(DatatypeVariable datatypeVariable);
    O visit(DatatypeRestriction datatypeRestriction);
    O visit(FacetRestriction facetRestriction);
    O visit(DataComplementOf dataComplementOf);
    O visit(DataIntersectionOf dataIntersectionOf);
    O visit(DataUnionOf dataUnionOf);
    O visit(DataOneOf dataOneOf);
    
    O visit(Annotation annotation);
    O visit(AnnotationValue annotationValue);
    
    O visit(SubClassOf axiom);
    O visit(EquivalentClasses axiom);
    O visit(DisjointClasses axiom);
    O visit(DisjointUnion axiom);
    
    O visit(SubObjectPropertyOf axiom);
    O visit(EquivalentObjectProperties axiom);
    O visit(DisjointObjectProperties axiom);
    O visit(InverseObjectProperties axiom);
    O visit(ObjectPropertyDomain axiom);
    O visit(ObjectPropertyRange axiom);
    O visit(FunctionalObjectProperty axiom);
    O visit(InverseFunctionalObjectProperty axiom);
    O visit(ReflexiveObjectProperty axiom);
    O visit(IrreflexiveObjectProperty axiom);
    O visit(SymmetricObjectProperty axiom);
    O visit(AsymmetricObjectProperty axiom);
    O visit(TransitiveObjectProperty axiom);
    
    O visit(SubDataPropertyOf axiom);
    O visit(EquivalentDataProperties axiom);
    O visit(DisjointDataProperties axiom);
    O visit(DataPropertyDomain axiom);
    O visit(DataPropertyRange axiom);
    O visit(FunctionalDataProperty axiom);
    
    O visit(DatatypeDefinition axiom);
    
    O visit(HasKey axiom);
    
    O visit(SameIndividual axiom);
    O visit(DifferentIndividuals axiom);
    O visit(ClassAssertion axiom);
    O visit(ObjectPropertyAssertion axiom);
    O visit(NegativeObjectPropertyAssertion axiom);
    O visit(DataPropertyAssertion axiom);
    O visit(NegativeDataPropertyAssertion axiom);
}