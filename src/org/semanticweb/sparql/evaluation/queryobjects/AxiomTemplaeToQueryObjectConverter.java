package org.semanticweb.sparql.evaluation.queryobjects;

import java.util.Map;

import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.sparql.arq.HermiTGraph;
import org.semanticweb.sparql.owlbgp.model.AxiomVisitorEx;
import org.semanticweb.sparql.owlbgp.model.Import;
import org.semanticweb.sparql.owlbgp.model.Variable;
import org.semanticweb.sparql.owlbgp.model.axioms.AnnotationAssertion;
import org.semanticweb.sparql.owlbgp.model.axioms.AnnotationPropertyDomain;
import org.semanticweb.sparql.owlbgp.model.axioms.AnnotationPropertyRange;
import org.semanticweb.sparql.owlbgp.model.axioms.AsymmetricObjectProperty;
import org.semanticweb.sparql.owlbgp.model.axioms.Axiom;
import org.semanticweb.sparql.owlbgp.model.axioms.ClassAssertion;
import org.semanticweb.sparql.owlbgp.model.axioms.DataPropertyAssertion;
import org.semanticweb.sparql.owlbgp.model.axioms.DataPropertyDomain;
import org.semanticweb.sparql.owlbgp.model.axioms.DataPropertyRange;
import org.semanticweb.sparql.owlbgp.model.axioms.DatatypeDefinition;
import org.semanticweb.sparql.owlbgp.model.axioms.Declaration;
import org.semanticweb.sparql.owlbgp.model.axioms.DifferentIndividuals;
import org.semanticweb.sparql.owlbgp.model.axioms.DisjointClasses;
import org.semanticweb.sparql.owlbgp.model.axioms.DisjointDataProperties;
import org.semanticweb.sparql.owlbgp.model.axioms.DisjointObjectProperties;
import org.semanticweb.sparql.owlbgp.model.axioms.DisjointUnion;
import org.semanticweb.sparql.owlbgp.model.axioms.EquivalentClasses;
import org.semanticweb.sparql.owlbgp.model.axioms.EquivalentDataProperties;
import org.semanticweb.sparql.owlbgp.model.axioms.EquivalentObjectProperties;
import org.semanticweb.sparql.owlbgp.model.axioms.FunctionalDataProperty;
import org.semanticweb.sparql.owlbgp.model.axioms.FunctionalObjectProperty;
import org.semanticweb.sparql.owlbgp.model.axioms.HasKey;
import org.semanticweb.sparql.owlbgp.model.axioms.InverseFunctionalObjectProperty;
import org.semanticweb.sparql.owlbgp.model.axioms.InverseObjectProperties;
import org.semanticweb.sparql.owlbgp.model.axioms.IrreflexiveObjectProperty;
import org.semanticweb.sparql.owlbgp.model.axioms.NegativeDataPropertyAssertion;
import org.semanticweb.sparql.owlbgp.model.axioms.NegativeObjectPropertyAssertion;
import org.semanticweb.sparql.owlbgp.model.axioms.ObjectPropertyAssertion;
import org.semanticweb.sparql.owlbgp.model.axioms.ObjectPropertyDomain;
import org.semanticweb.sparql.owlbgp.model.axioms.ObjectPropertyRange;
import org.semanticweb.sparql.owlbgp.model.axioms.ReflexiveObjectProperty;
import org.semanticweb.sparql.owlbgp.model.axioms.SameIndividual;
import org.semanticweb.sparql.owlbgp.model.axioms.SubAnnotationPropertyOf;
import org.semanticweb.sparql.owlbgp.model.axioms.SubClassOf;
import org.semanticweb.sparql.owlbgp.model.axioms.SubDataPropertyOf;
import org.semanticweb.sparql.owlbgp.model.axioms.SubObjectPropertyOf;
import org.semanticweb.sparql.owlbgp.model.axioms.SymmetricObjectProperty;
import org.semanticweb.sparql.owlbgp.model.axioms.TransitiveObjectProperty;

public class AxiomTemplaeToQueryObjectConverter implements AxiomVisitorEx<QueryObject<? extends Axiom>> {
    protected final OWLDataFactory dataFactory;
    protected final Map<Variable,Integer> positionInTuple;
    protected final HermiTGraph graph;
    
    public AxiomTemplaeToQueryObjectConverter(OWLDataFactory dataFactory, Map<Variable,Integer> positionInTuple, HermiTGraph graph) {
        this.dataFactory=dataFactory;
        this.positionInTuple=positionInTuple;
        this.graph=graph;
    }
    public QueryObject<Import> visit(Import axiom) {
        return null;
    }
    public QueryObject<SubAnnotationPropertyOf> visit(SubAnnotationPropertyOf axiom) {
        return null;
    }
    public QueryObject<AnnotationPropertyDomain> visit(AnnotationPropertyDomain axiom) {
        return null;
    }
    public QueryObject<AnnotationPropertyRange> visit(AnnotationPropertyRange axiom) {
        return null;
    }
    public QueryObject<AnnotationAssertion> visit(AnnotationAssertion axiom) {
        return null;
    }
    public QueryObject<Declaration> visit(Declaration axiom) {
        return null;
    }
    public QueryObject<SubClassOf> visit(SubClassOf axiom) {
        return new QO_SubClassOf(axiom);
    }
    public QueryObject<EquivalentClasses> visit(EquivalentClasses axiom) {
        return null;
    }
    public QueryObject<DisjointClasses> visit(DisjointClasses axiom) {
        return null;
    }
    public QueryObject<DisjointUnion> visit(DisjointUnion axiom) {
        return null;
    }
    public QueryObject<SubObjectPropertyOf> visit(SubObjectPropertyOf axiom) {
        return null;
    }
    public QueryObject<EquivalentObjectProperties> visit(EquivalentObjectProperties axiom) {
        return null;
    }
    public QueryObject<DisjointObjectProperties> visit(DisjointObjectProperties axiom) {
        return null;
    }
    public QueryObject<InverseObjectProperties> visit(InverseObjectProperties axiom) {
        return null;
    }
    public QueryObject<ObjectPropertyDomain> visit(ObjectPropertyDomain axiom) {
        return null;
    }
    public QueryObject<ObjectPropertyRange> visit(ObjectPropertyRange axiom) {
        return null;
    }
    public QueryObject<FunctionalObjectProperty> visit(FunctionalObjectProperty axiom) {
        return new QO_FunctionalObjectProperty(axiom);
    }
    public QueryObject<InverseFunctionalObjectProperty> visit(InverseFunctionalObjectProperty axiom) {
        return new QO_InverseFunctionalObjectProperty(axiom);
    }
    public QueryObject<ReflexiveObjectProperty> visit(ReflexiveObjectProperty axiom) {
        return new QO_ReflexiveObjectProperty(axiom);
    }
    public QueryObject<IrreflexiveObjectProperty> visit(IrreflexiveObjectProperty axiom) {
        return new QO_IrreflexiveObjectProperty(axiom);
    }
    public QueryObject<SymmetricObjectProperty> visit(SymmetricObjectProperty axiom) {
        return new QO_SymmetricObjectProperty(axiom);
    }
    public QueryObject<AsymmetricObjectProperty> visit(AsymmetricObjectProperty axiom) {
        return new QO_AsymmetricObjectProperty(axiom);
    }
    public QueryObject<TransitiveObjectProperty> visit(TransitiveObjectProperty axiom) {
        return new QO_TransitiveObjectProperty(axiom);
    }
    public QueryObject<SubDataPropertyOf> visit(SubDataPropertyOf axiom) {
        return null;
    }
    public QueryObject<EquivalentDataProperties> visit(EquivalentDataProperties axiom) {
        return null;
    }
    public QueryObject<DisjointDataProperties> visit(DisjointDataProperties axiom) {
        return null;
    }
    public QueryObject<DataPropertyDomain> visit(DataPropertyDomain axiom) {
        return null;
    }
    public QueryObject<DataPropertyRange> visit(DataPropertyRange axiom) {
        return null;
    }
    public QueryObject<FunctionalDataProperty> visit(FunctionalDataProperty axiom) {
        return null;
    }
    public QueryObject<DatatypeDefinition> visit(DatatypeDefinition axiom) {
        return null;
    }
    public QueryObject<HasKey> visit(HasKey axiom) {
        return null;
    }
    public QueryObject<SameIndividual> visit(SameIndividual axiom) {
        return null;
    }
    public QueryObject<DifferentIndividuals> visit(DifferentIndividuals axiom) {
        return null;
    }
    public QueryObject<ClassAssertion> visit(ClassAssertion axiom) {
        return new QO_ClassAssertion(axiom);
    }
    public QueryObject<ObjectPropertyAssertion> visit(ObjectPropertyAssertion axiom) {
        return new QO_ObjectPropertyAssertion(axiom);
    }
    public QueryObject<NegativeObjectPropertyAssertion> visit(NegativeObjectPropertyAssertion axiom) {
        return null;
    }
    public QueryObject<DataPropertyAssertion> visit(DataPropertyAssertion axiom) {
        return null;
    }
    public QueryObject<NegativeDataPropertyAssertion> visit(NegativeDataPropertyAssertion axiom) {
        return null;
    }
}
