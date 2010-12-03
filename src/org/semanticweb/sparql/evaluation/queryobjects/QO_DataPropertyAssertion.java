package org.semanticweb.sparql.evaluation.queryobjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.sparql.evaluation.HermiTGraph;
import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.Variable;
import org.semanticweb.sparql.owlbgp.model.axioms.DataPropertyAssertion;
import org.semanticweb.sparql.owlbgp.model.dataranges.Datatype;
import org.semanticweb.sparql.owlbgp.model.individuals.Individual;
import org.semanticweb.sparql.owlbgp.model.individuals.IndividualVariable;
import org.semanticweb.sparql.owlbgp.model.literals.Literal;
import org.semanticweb.sparql.owlbgp.model.literals.TypedLiteral;
import org.semanticweb.sparql.owlbgp.model.properties.DataPropertyVariable;

public class QO_DataPropertyAssertion  extends AbstractQueryObject<DataPropertyAssertion> {

    public QO_DataPropertyAssertion(DataPropertyAssertion axiomTemplate) {
        super(axiomTemplate);
    }
    protected List<Atomic[]> addBindings(Reasoner reasoner, OWLDataFactory dataFactory, HermiTGraph graph, Atomic[] currentBinding, Map<Variable,Integer> bindingPositions) {
        Map<Variable,Atomic> bindingMap=new HashMap<Variable, Atomic>();
        // apply bindings that are already computed from previous steps
        for (Variable var : bindingPositions.keySet())
            bindingMap.put(var, currentBinding[bindingPositions.get(var)]);
        try {
            DataPropertyAssertion assertion=((DataPropertyAssertion)m_axiomTemplate.getBoundVersion(bindingMap));
            Atomic dpe=(Atomic)assertion.getDataPropertyExpression();
            Individual ind=assertion.getIndividual();
            Literal lit=assertion.getLiteral();
            if (dpe.isVariable() && ind.isVariable() && lit.isVariable()) { 
                // ?x(?y ?z)
                int[] positions=new int[3];
                positions[0]=bindingPositions.get(dpe);
                positions[1]=bindingPositions.get(ind);
                positions[2]=bindingPositions.get(lit);
                return compute012UnBound(reasoner,currentBinding,positions);
            } else if (dpe.isVariable() && ind.isVariable() && !lit.isVariable()) {
                // ?x(?y lit)
                int[] positions=new int[2];
                positions[0]=bindingPositions.get(dpe);
                positions[1]=bindingPositions.get(ind);
                return compute2Bound(reasoner,currentBinding,(OWLLiteral)lit.asOWLAPIObject(dataFactory),positions);
            } else if (dpe.isVariable() && !ind.isVariable() && lit.isVariable()) {
                // ?x(:a ?z)
                int[] positions=new int[2];
                positions[0]=bindingPositions.get(dpe);
                positions[1]=bindingPositions.get(lit);
                return compute1Bound(reasoner,currentBinding,(OWLNamedIndividual)ind.asOWLAPIObject(dataFactory),positions);
            } else if (dpe.isVariable() && !ind.isVariable() && !lit.isVariable()) {
                // ?x(:a lit)
                int[] positions=new int[1];
                positions[0]=bindingPositions.get(dpe);
                return compute12Bound(reasoner,currentBinding,(OWLNamedIndividual)ind.asOWLAPIObject(dataFactory),(OWLLiteral)lit.asOWLAPIObject(dataFactory),positions);
            } else if (!dpe.isVariable() && ind.isVariable() && lit.isVariable()) {
                // dp(?y ?z)
                int[] positions=new int[2];
                positions[0]=bindingPositions.get(ind);
                positions[1]=bindingPositions.get(lit);
                return compute0Bound(reasoner,currentBinding,(OWLDataProperty)dpe.asOWLAPIObject(dataFactory),positions);
            } else if (!dpe.isVariable() && ind.isVariable() && !lit.isVariable()) {
                // dp(?y lit)
                int[] positions=new int[1];
                positions[0]=bindingPositions.get(ind);
                return compute02Bound(reasoner,currentBinding,(OWLDataProperty)dpe.asOWLAPIObject(dataFactory),(OWLLiteral)lit.asOWLAPIObject(dataFactory),positions);
            } else if (!dpe.isVariable() && !ind.isVariable() && lit.isVariable()) {
                // dp(:a ?z)
                int[] positions=new int[1];
                positions[0]=bindingPositions.get(lit);
                return compute01Bound(reasoner,currentBinding,(OWLDataProperty)dpe.asOWLAPIObject(dataFactory),(OWLNamedIndividual)ind.asOWLAPIObject(dataFactory),positions);
            } else if (!dpe.isVariable() && !ind.isVariable() && !lit.isVariable()) {
                // dp(:a lit)
                return compute012Bound(reasoner,currentBinding,(OWLDataProperty)dpe.asOWLAPIObject(dataFactory),(OWLNamedIndividual)ind.asOWLAPIObject(dataFactory),(OWLLiteral)lit.asOWLAPIObject(dataFactory));
            } else {
                return complex(reasoner,dataFactory,graph,currentBinding,assertion,bindingPositions);
            }
        } catch (IllegalArgumentException e) {
            // current binding is incompatible will not add new bindings in newBindings
            return new ArrayList<Atomic[]>();
        }
    }
    protected List<Atomic[]> compute012UnBound(Reasoner reasoner,Atomic[] currentBinding, int[] bindingPositions) {
        // DataPropertyAssertion(?x ?y ?z)
        List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
        OWLOntology ont=reasoner.getRootOntology();
        for (OWLDataProperty owlDp : ont.getDataPropertiesInSignature(true))
            newBindings.addAll(compute0Bound(reasoner, currentBinding, owlDp, bindingPositions));
        return newBindings;
    }
    protected List<Atomic[]> compute2Bound(Reasoner reasoner, Atomic[] currentBinding, OWLLiteral lit, int[] bindingPositions) {
        // DataPropertyAssertion(?x ?y :a)
        List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
        OWLOntology ont=reasoner.getRootOntology();
        for (OWLDataProperty owlDp : ont.getDataPropertiesInSignature(true)) {
            newBindings.addAll(compute02Bound(reasoner, currentBinding, owlDp, lit, bindingPositions));
        }
        return newBindings;
    }
    protected List<Atomic[]> compute1Bound(Reasoner reasoner, Atomic[] currentBinding, OWLNamedIndividual ind, int[] bindingPositions) {
        // DataPropertyAssertion(?x :a ?y)
        List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
        OWLOntology ont=reasoner.getRootOntology();
        for (OWLDataProperty owlDp : ont.getDataPropertiesInSignature(true))
            newBindings.addAll(compute01Bound(reasoner, currentBinding, owlDp, ind, bindingPositions));
        return newBindings;
    }
    protected List<Atomic[]> compute12Bound(Reasoner reasoner, Atomic[] currentBinding, OWLNamedIndividual ind, OWLLiteral lit, int[] bindingPositions) {
        // DataPropertyAssertion(?x :a :b)
        List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
        for (OWLDataProperty owlDp : reasoner.getRootOntology().getDataPropertiesInSignature(true))
            newBindings.addAll(compute012Bound(reasoner, currentBinding, owlDp, ind, lit));
        return newBindings;
    }
    protected List<Atomic[]> compute0Bound(Reasoner reasoner, Atomic[] currentBinding, OWLDataProperty dpe, int[] bindingPositions) {
        // DataPropertyAssertion(:r ?x ?y)
        List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
        OWLOntology ont=reasoner.getRootOntology();
        for (OWLNamedIndividual ind : ont.getIndividualsInSignature(true))
            newBindings.addAll(compute01Bound(reasoner, currentBinding, dpe, ind, bindingPositions));
        return newBindings;
    }
    protected List<Atomic[]> compute02Bound(Reasoner reasoner, Atomic[] currentBinding, OWLDataProperty dpe, OWLLiteral lit, int[] bindingPositions) {
        // DataPropertyAssertion(:r ?x :a)
        List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
        OWLOntology ont=reasoner.getRootOntology();
        for (OWLNamedIndividual ind : ont.getIndividualsInSignature(true)) 
            newBindings.addAll(compute012Bound(reasoner, currentBinding, dpe, ind, lit));
        return newBindings;
    }
    protected List<Atomic[]> compute01Bound(Reasoner reasoner, Atomic[] currentBinding, OWLDataProperty dpe, OWLNamedIndividual ind, int[] bindingPositions) {
        // DataPropertyAssertion(:r :a ?y)
        Atomic[] binding;
        List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
        for (OWLLiteral lit : reasoner.getDataPropertyValues(ind, dpe)) {
            binding=currentBinding.clone();
            binding[bindingPositions[0]]=TypedLiteral.create(lit.getLiteral(), lit.getLang(), Datatype.create(lit.getDatatype().getIRI().toString()));
            newBindings.add(binding);
        }
        return newBindings;
    }
    protected List<Atomic[]> compute012Bound(Reasoner reasoner, Atomic[] currentBinding, OWLDataProperty ope, OWLNamedIndividual ind, OWLLiteral lit) {
        // DataPropertyAssertion(:r :a :b)
        List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
        if (reasoner.hasDataPropertyRelationship(ind, ope, lit))
            newBindings.add(currentBinding);
        return newBindings;
    }
    public int getCurrentCost(Reasoner reasoner, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions, HermiTGraph graph) {
        int cost=0;
        if (candidateBindings.isEmpty())
            return cost; // no answers, no tests
        for (Atomic[] testBinding : candidateBindings) { 
            Set<Variable> vars=m_axiomTemplate.getVariablesInSignature();
            Variable dpVar=null;
            Variable indVar=null;
            for (Variable v : vars) {
                if (v instanceof IndividualVariable)
                    indVar=v;
                else if (v instanceof DataPropertyVariable)
                    dpVar=v;
            }
            Map<Variable,Atomic> existingBindings=new HashMap<Variable,Atomic>();
            for (Variable var : vars) {
                Atomic binding=testBinding[bindingPositions.get(var)];
                if (binding!=null)
                    existingBindings.put(var,binding);
            }
            vars.removeAll(existingBindings.keySet());
            if (vars.size()==0)
                return candidateBindings.size()*COST_LOOKUP; // check entailment for each candidate binding, might have to really test, not just lookup
            else if (vars.size()==2) {
                if (dpVar!=null) {
                    // r(?x ?y)
                    cost+=graph.getIndividualsInSignature().size()*COST_LOOKUP; // although we might have to do subProperty reasoning and sameAs
                } else if (indVar!=null) {
                    // ?x(:a ?y)
                    cost+=graph.getDataPropertiesInSignature().size()*COST_LOOKUP;// although we might have to do subProperty reasoning and sameAs
                } else {
                    // ?x(?y :a)
                    cost+=graph.getDataPropertiesInSignature().size()*graph.getIndividualsInSignature().size()*COST_LOOKUP;
                }
            } else if (vars.size()==1) {
                if (dpVar!=null) {
                    // ?x(:a :b)
                    cost+=(graph.getDataPropertiesInSignature().size()*COST_LOOKUP); // might just do lookup though
                } else if (indVar!=null) {
                    // r(?x :a)
                    cost+=(graph.getIndividualsInSignature().size()*COST_LOOKUP); // might just do lookup though
                } else {
                    // r(:a ?x)
                    cost+=COST_LOOKUP; // might just do lookup though
                }
            } else {
                // ?x(?y ?z)
                cost+=graph.getDataPropertiesInSignature().size()*graph.getIndividualsInSignature().size()*COST_LOOKUP;
            }
        }
        return cost;
    }
}
