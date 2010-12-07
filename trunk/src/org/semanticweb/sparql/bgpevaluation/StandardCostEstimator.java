package org.semanticweb.sparql.bgpevaluation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.sparql.arq.OWLOntologyGraph;
import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.Import;
import org.semanticweb.sparql.owlbgp.model.Variable;
import org.semanticweb.sparql.owlbgp.model.axioms.AnnotationAssertion;
import org.semanticweb.sparql.owlbgp.model.axioms.AnnotationPropertyDomain;
import org.semanticweb.sparql.owlbgp.model.axioms.AnnotationPropertyRange;
import org.semanticweb.sparql.owlbgp.model.axioms.AsymmetricObjectProperty;
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
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassVariable;
import org.semanticweb.sparql.owlbgp.model.dataranges.DatatypeVariable;
import org.semanticweb.sparql.owlbgp.model.individuals.Individual;
import org.semanticweb.sparql.owlbgp.model.individuals.IndividualVariable;
import org.semanticweb.sparql.owlbgp.model.properties.AnnotationPropertyVariable;
import org.semanticweb.sparql.owlbgp.model.properties.DataPropertyVariable;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyVariable;

public class StandardCostEstimator {
    protected int COST_ENTAILMENT=100;
    protected int COST_LOOKUP=1;
    protected int COST_CLASS_HIERARCHY_INSERTION=10*COST_ENTAILMENT;
    
    protected final OWLReasoner m_reasoner;
    protected final OWLDataFactory m_dataFactory;
    protected final OWLOntologyGraph m_graph;
    protected final int m_classCount;
    protected final int m_opCount;
    protected final int m_dpCount;
    protected final int m_apCount;
    protected final int m_datatypeCount;
    protected final int m_indCount;
    
    public StandardCostEstimator(OWLOntologyGraph graph) {
        m_reasoner=graph.getReasoner();
        m_dataFactory=graph.getDefaultOntology().getOWLOntologyManager().getOWLDataFactory();
        m_graph=graph;
        m_classCount=graph.getClassesInSignature().size();
        m_opCount=graph.getObjectPropertiesInSignature().size();
        m_dpCount=graph.getDataPropertiesInSignature().size();
        m_apCount=graph.getAnnotationPropertiesInSignature().size();
        m_datatypeCount=graph.getDatatypesInSignature().size();
        m_indCount=graph.getIndividualsInSignature().size();
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
    public int[] getCost(ClassAssertion axiom, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions) {
        int[] estimate=new int[2];
        if (candidateBindings.isEmpty())
            return estimate; // no answers, no tests
        Set<Variable> vars=axiom.getVariablesInSignature();
        Set<Variable> indVars=axiom.getIndividual().getVariablesInSignature();
        Variable indVar=indVars.isEmpty()?null:indVars.iterator().next();
        Map<Variable,Atomic> existingBindings=new HashMap<Variable,Atomic>();
        Set<Variable> unbound=new HashSet<Variable>();
        for (Atomic[] testBinding : candidateBindings) {
            existingBindings.clear();
            for (Variable var : vars) {
                Atomic binding=testBinding[bindingPositions.get(var)];
                if (binding!=null)
                    existingBindings.put(var,binding);
            }
            ClassAssertion instantiated=(ClassAssertion)axiom.getBoundVersion(existingBindings);
            unbound.addAll(vars);
            unbound.removeAll(existingBindings.keySet());
            
            int[] currentEstimate=getClassAssertionCost(instantiated.getClassExpression(), instantiated.getIndividual(), unbound, indVar);
            estimate[0]+=currentEstimate[0];
            estimate[1]+=currentEstimate[1];
        }
        return estimate;
    }
    protected int[] getClassAssertionCost(ClassExpression ce, Individual ind, Set<Variable> unbound, Variable indVar) {
        if (unbound.size()==0)
            return new int[] { COST_ENTAILMENT, 1 };
        else if (unbound.size()==1 && indVar!=null) // C(?x)
            return new int[] { m_indCount * COST_ENTAILMENT, m_indCount };
        else if (indVar==null && unbound.size()==1 && ce instanceof Atomic) // ?x(:a)
            return new int[] { m_classCount * COST_ENTAILMENT, m_classCount };
        else {
            int tests=complex(unbound);
            return new int[] { tests, tests }; // assume all tests succeed
        }
    }
    public int[] getCost(ObjectPropertyAssertion axiom, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions) {
//        int cost=0;
//        if (candidateBindings.isEmpty())
//            return cost; // no answers, no tests
//        OWLDataFactory dataFactory=reasoner.getDataFactory();
//        for (Atomic[] testBinding : candidateBindings) { 
//            Set<Variable> opVars=m_axiomTemplate.getObjectPropertyExpression().getVariablesInSignature();
//            Set<Variable> indVars=m_axiomTemplate.getVariablesInSignature();
//            Map<Variable,Atomic> existingBindings=new HashMap<Variable,Atomic>();
//            for (Variable var : indVars) {
//                Atomic binding=testBinding[bindingPositions.get(var)];
//                if (binding!=null)
//                    existingBindings.put(var,binding);
//            }
//            Set<Variable> unbound=new HashSet<Variable>(indVars);
//            indVars.removeAll(opVars);
//            Variable opVar=opVars.isEmpty()?null:opVars.iterator().next();
//            unbound.removeAll(existingBindings.keySet());
//            ObjectPropertyAssertion normalized=m_axiomTemplate.getNormalizedAssertion();
//            if (unbound.size()==0)
//                return candidateBindings.size()*COST_LOOKUP; // check entailment for each candidate binding, might have to really test, not just lookup
//            else if (unbound.size()==2) {
//                if (opVar!=null) {
//                    // r(?x ?y)
//                    ObjectProperty op=(ObjectProperty)normalized.getObjectPropertyExpression();
//                    int[] estimate=reasoner.getNumberOfInstances((OWLObjectProperty)op.getBoundVersion(existingBindings,dataFactory));
//                    cost+=estimate[0]*COST_LOOKUP;
//                    cost+=estimate[1]*COST_ENTAILMENT; // although we might have to do several tests
//                } else if (unbound.contains(normalized.getIndividual2())) {
//                    // ?x(:a ?y)
//                    for (ObjectProperty op : graph.getObjectPropertiesInSignature()) {
//                        int[] estimate=reasoner.getNumberOfSuccessors((OWLObjectProperty)op.getBoundVersion(existingBindings,dataFactory), (OWLNamedIndividual)normalized.getIndividual1().getBoundVersion(existingBindings,dataFactory));
//                        cost+=estimate[0]*COST_LOOKUP;
//                        cost+=estimate[1]*COST_ENTAILMENT; // although we might have to do several tests
//                    }
//                } else {
//                    // ?x(?y :a)
//                    for (ObjectProperty op : graph.getObjectPropertiesInSignature()) {
//                        int[] estimate=reasoner.getNumberOfPredecessors((OWLObjectProperty)op.getBoundVersion(existingBindings,dataFactory), (OWLNamedIndividual)normalized.getIndividual2().getBoundVersion(existingBindings,dataFactory));
//                        cost+=estimate[0]*COST_LOOKUP;
//                        cost+=estimate[1]*COST_ENTAILMENT; // although we might have to do several tests
//                    }
//                }
//            } else if (unbound.size()==1) {
//                if (opVar==null) {
//                    // ?x(:a :b)
//                    cost+=(graph.getObjectPropertiesInSignature().size()*COST_ENTAILMENT); // might just do lookup though
//                } else if (unbound.contains(normalized.getIndividual1())) {
//                    // r(?x :a)
//                    int[] estimate=reasoner.getNumberOfPredecessors((OWLObjectProperty)normalized.getObjectPropertyExpression().getBoundVersion(existingBindings, dataFactory), (OWLNamedIndividual)normalized.getIndividual2().getBoundVersion(existingBindings,dataFactory));
//                    cost+=estimate[0]*COST_LOOKUP;
//                    cost+=estimate[1]*COST_ENTAILMENT; // although we might have to do several tests
//                }
//            } else {
//                // ?x(?y ?z)
//                for (ObjectProperty op : graph.getObjectPropertiesInSignature()) {
//                    int[] estimate=reasoner.getNumberOfInstances((OWLObjectProperty)op.getBoundVersion(existingBindings,dataFactory));
//                    cost+=estimate[0]*COST_LOOKUP;
//                    cost+=estimate[1]*COST_ENTAILMENT; // although we might have to do several tests
//                }
//            }
//        }
//        return cost;
        return null;
    }
    public int[] getCost(NegativeObjectPropertyAssertion axiom, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions) {
        return null;
    }
    public int[] getCost(DataPropertyAssertion axiom, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions) {
//        int cost=0;
//        if (candidateBindings.isEmpty())
//            return cost; // no answers, no tests
//        for (Atomic[] testBinding : candidateBindings) { 
//            Set<Variable> vars=m_axiomTemplate.getVariablesInSignature();
//            Variable dpVar=null;
//            Variable indVar=null;
//            for (Variable v : vars) {
//                if (v instanceof IndividualVariable)
//                    indVar=v;
//                else if (v instanceof DataPropertyVariable)
//                    dpVar=v;
//            }
//            Map<Variable,Atomic> existingBindings=new HashMap<Variable,Atomic>();
//            for (Variable var : vars) {
//                Atomic binding=testBinding[bindingPositions.get(var)];
//                if (binding!=null)
//                    existingBindings.put(var,binding);
//            }
//            vars.removeAll(existingBindings.keySet());
//            if (vars.size()==0)
//                return candidateBindings.size()*COST_LOOKUP; // check entailment for each candidate binding, might have to really test, not just lookup
//            else if (vars.size()==2) {
//                if (dpVar!=null) {
//                    // r(?x ?y)
//                    cost+=graph.getIndividualsInSignature().size()*COST_LOOKUP; // although we might have to do subProperty reasoning and sameAs
//                } else if (indVar!=null) {
//                    // ?x(:a ?y)
//                    cost+=graph.getDataPropertiesInSignature().size()*COST_LOOKUP;// although we might have to do subProperty reasoning and sameAs
//                } else {
//                    // ?x(?y :a)
//                    cost+=graph.getDataPropertiesInSignature().size()*graph.getIndividualsInSignature().size()*COST_LOOKUP;
//                }
//            } else if (vars.size()==1) {
//                if (dpVar!=null) {
//                    // ?x(:a :b)
//                    cost+=(graph.getDataPropertiesInSignature().size()*COST_LOOKUP); // might just do lookup though
//                } else if (indVar!=null) {
//                    // r(?x :a)
//                    cost+=(graph.getIndividualsInSignature().size()*COST_LOOKUP); // might just do lookup though
//                } else {
//                    // r(:a ?x)
//                    cost+=COST_LOOKUP; // might just do lookup though
//                }
//            } else {
//                // ?x(?y ?z)
//                cost+=graph.getDataPropertiesInSignature().size()*graph.getIndividualsInSignature().size()*COST_LOOKUP;
//            }
//        }
//        return cost;
        return null;
    }
    public int[] getCost(NegativeDataPropertyAssertion axiom, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions) {
        return null;
    }
    protected int complex(Set<Variable> unbound) {
        int tests=0;
        // complex
        boolean first=true;
        for (Variable var : unbound) {
            int signatureSize=0;
            if (var instanceof ClassVariable)
                signatureSize=m_classCount;
            else if (var instanceof DatatypeVariable)
                signatureSize=m_datatypeCount;
            else if (var instanceof ObjectPropertyVariable)
                signatureSize=m_opCount;
            else if (var instanceof DataPropertyVariable)
                signatureSize=m_dpCount;
            else if (var instanceof AnnotationPropertyVariable)
                signatureSize=m_apCount;
            else if (var instanceof IndividualVariable)
                signatureSize=m_indCount;
            if (first)
                tests+=signatureSize;
            else 
                tests*=signatureSize;
        }
        return tests;
    }
    protected int[] getObjectPropertyAxiomCost() {
//        int cost=0;
//        if (candidateBindings.isEmpty())
//            return cost; // no answers, no tests
//        // check just one binding
//        Atomic[] testBinding=candidateBindings.get(0);
//        Set<Variable> opeVar=m_axiomTemplate.getObjectPropertyExpression().getVariablesInSignature();
//        boolean hasUnboundVar=false;
//        if (!opeVar.isEmpty() && testBinding[bindingPositions.get(opeVar.iterator().next())]!=null)
//            hasUnboundVar=true;
//        if (hasUnboundVar)
//            return reasoner.getRootOntology().getObjectPropertiesInSignature(true).size()*COST_ENTAILMENT; // check entailment for each candidate binding, might have to really test, not just lookup
//        else 
//            return candidateBindings.size()*COST_ENTAILMENT;
        return null;
    }
}
