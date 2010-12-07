package org.semanticweb.HermiT;

import java.util.List;
import java.util.Map;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.sparql.arq.OWLOntologyGraph;
import org.semanticweb.sparql.bgpevaluation.StandardCostEstimator;
import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.Import;
import org.semanticweb.sparql.owlbgp.model.Variable;
import org.semanticweb.sparql.owlbgp.model.axioms.AnnotationAssertion;
import org.semanticweb.sparql.owlbgp.model.axioms.AnnotationPropertyDomain;
import org.semanticweb.sparql.owlbgp.model.axioms.AnnotationPropertyRange;
import org.semanticweb.sparql.owlbgp.model.axioms.AsymmetricObjectProperty;
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

public class HermiTCostEstimator extends StandardCostEstimator {
    protected int COST_ENTAILMENT=100;
    protected int COST_LOOKUP=1;
    protected int COST_CLASS_HIERARCHY_INSERTION=10*COST_ENTAILMENT;
    
    protected final Reasoner m_hermit;
    
    public HermiTCostEstimator(OWLOntologyGraph graph) {
        super(graph);
        if (m_reasoner instanceof Reasoner)
            m_hermit=(Reasoner)m_reasoner;
        else 
            throw new IllegalArgumentException("Error: The HermiT cost estimator can only be instantiated with a graph that has a (HermiT) Reasoner instance attached to it.");
    }
    public int[] getCost(Import axiom, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions) {
        return null;
    }
    public int[] getCost(SubAnnotationPropertyOf axiom, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions) {
        return null;
    }
    public int[] getCost(AnnotationPropertyDomain axiom, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions) {
        return null;
    }
    public int[] getCost(AnnotationPropertyRange axiom, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions) {
        return null;
    }
    public int[] getCost(AnnotationAssertion axiom, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions) {
        return null;
    }
    public int[] getCost(Declaration axiom, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions) {
        return null;
    }
    public int[] getCost(SubClassOf axiom, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions) {
        return null;
    }
    public int[] getCost(EquivalentClasses axiom, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions) {
        return null;
    }
    public int[] getCost(DisjointClasses axiom, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions) {
        return null;
    }
    public int[] getCost(DisjointUnion axiom, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions) {
        return null;
    }
    public int[] getCost(SubObjectPropertyOf axiom, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions) {
        return null;
    }
    public int[] getCost(EquivalentObjectProperties axiom, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions) {
        return null;
    }
    public int[] getCost(DisjointObjectProperties axiom, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions) {
        return null;
    }
    public int[] getCost(InverseObjectProperties axiom, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions) {
        return null;
    }
    public int[] getCost(ObjectPropertyDomain axiom, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions) {
        return null;
    }
    public int[] getCost(ObjectPropertyRange axiom, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions) {
        return null;
    }
    public int[] getCost(FunctionalObjectProperty axiom, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions) {
        return null;
    }
    public int[] getCost(InverseFunctionalObjectProperty axiom, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions) {
        return null;
    }
    public int[] getCost(ReflexiveObjectProperty axiom, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions) {
        return null;
    }
    public int[] getCost(IrreflexiveObjectProperty axiom, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions) {
        return null;
    }
    public int[] getCost(SymmetricObjectProperty axiom, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions) {
        return null;
    }
    public int[] getCost(AsymmetricObjectProperty axiom, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions) {
        return null;
    }
    public int[] getCost(TransitiveObjectProperty axiom, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions) {
        return null;
    }
    public int[] getCost(SubDataPropertyOf axiom, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions) {
        return null;
    }
    public int[] getCost(EquivalentDataProperties axiom, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions) {
        return null;
    }
    public int[] getCost(DisjointDataProperties axiom, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions) {
        return null;
    }
    public int[] getCost(DataPropertyDomain axiom, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions) {
        return null;
    }
    public int[] getCost(DataPropertyRange axiom, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions) {
        return null;
    }
    public int[] getCost(FunctionalDataProperty axiom, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions) {
        return null;
    }
    public int[] getCost(DatatypeDefinition axiom, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions) {
        return null;
    }
    public int[] getCost(HasKey axiom, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions) {
        return null;
    }
    public int[] getCost(SameIndividual axiom, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions) {
        return null;
    }
    public int[] getCost(DifferentIndividuals axiom, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions) {
        return null;
    }
//    protected int[] getClassAssertionCost(ClassExpression ce, Individual ind, Set<Variable> unbound, Variable indVar) {
//        if (unbound.size()==0)
//            m_hermit.
//            if (ce instanceof Atomic)
//                return COST_LOOKUP; 
//            else 
//                return COST_ENTAILMENT;
//        else if (unbound.size()==1 && indVar!=null) {
//            // C(?x)
//            if (ce instanceof Atomic)
//                return COST_LOOKUP;
//            else 
//                return COST_CLASS_HIERARCHY_INSERTION;
//        } else if (indVar==null && unbound.size()==1 && ce instanceof Atomic) {
//            // ?x(:a)
//            return COST_LOOKUP;
//        } 
//        return null;
//        if (unbound.size()==0 && ce instanceof Atomic)
//            return new int[] { COST_LOOKUP, 1 };
//        else if (unbound.size()==1 && indVar!=null) // C(?x)
//            return new int[] { m_indCount * COST_ENTAILMENT, m_indCount };
//        else if (indVar==null && unbound.size()==1 && ce instanceof Atomic) // ?x(:a)
//            return new int[] { m_classCount * COST_ENTAILMENT, m_classCount };
//        else {
//            int tests=complex(unbound);
//            return new int[] { tests, tests }; // assume all tests succeed
//        }
//    }
    public int[] getCost(ObjectPropertyAssertion axiom, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions) {
        return null;
    }
    public int[] getCost(NegativeObjectPropertyAssertion axiom, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions) {
        return null;
    }
    public int[] getCost(DataPropertyAssertion axiom, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions) {
        return null;
    }
    public int[] getCost(NegativeDataPropertyAssertion axiom, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions) {
        return null;
    }
}
