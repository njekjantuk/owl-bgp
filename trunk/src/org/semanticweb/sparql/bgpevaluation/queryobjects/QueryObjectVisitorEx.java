package org.semanticweb.sparql.bgpevaluation.queryobjects;


public interface QueryObjectVisitorEx<O> {  
    O visit(QO_SubClassOf axiom);
//    O visit(QO_EquivalentClasses axiom);
//    O visit(QO_DisjointClasses axiom);

    O visit(QO_SubObjectPropertyOf axiom);
//    O visit(QO_EquivalentObjectProperties axiom);
//    O visit(QO_DisjointObjectProperties axiom);
//    O visit(QO_InverseObjectProperties axiom);
//    O visit(QO_ObjectPropertyDomain axiom);
//    O visit(QO_ObjectPropertyRange axiom);
    O visit(QO_FunctionalObjectProperty axiom);
    O visit(QO_InverseFunctionalObjectProperty axiom);
    O visit(QO_ReflexiveObjectProperty axiom);
    O visit(QO_IrreflexiveObjectProperty axiom);
    O visit(QO_SymmetricObjectProperty axiom);
    O visit(QO_AsymmetricObjectProperty axiom);
    O visit(QO_TransitiveObjectProperty axiom);
    
//    O visit(QO_SubDataPropertyOf axiom);
//    O visit(QO_EquivalentDataProperties axiom);
//    O visit(QO_DisjointDataProperties axiom);
//    O visit(QO_DataPropertyDomain axiom);
//    O visit(QO_DataPropertyRange axiom);
    O visit(QO_FunctionalDataProperty axiom);
//    
//    O visit(QO_DatatypeDefinition axiom);
//    
//    O visit(QO_HasKey axiom);
//    
//    O visit(QO_SameIndividual axiom);
//    O visit(QO_DifferentIndividuals axiom);
    O visit(QO_ClassAssertion axiom);
    O visit(QO_ObjectPropertyAssertion axiom);
    O visit(QO_NegativeObjectPropertyAssertion axiom);
    O visit(QO_DataPropertyAssertion axiom);
    O visit(QO_NegativeDataPropertyAssertion axiom);
}