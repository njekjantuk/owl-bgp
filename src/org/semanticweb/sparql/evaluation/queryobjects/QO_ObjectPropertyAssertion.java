package org.semanticweb.sparql.evaluation.queryobjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.sparql.evaluation.HermiTGraph;
import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.Variable;
import org.semanticweb.sparql.owlbgp.model.axioms.ObjectPropertyAssertion;
import org.semanticweb.sparql.owlbgp.model.individuals.Individual;
import org.semanticweb.sparql.owlbgp.model.individuals.NamedIndividual;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectProperty;

public class QO_ObjectPropertyAssertion  extends AbstractQueryObject<ObjectPropertyAssertion> {

    public QO_ObjectPropertyAssertion(ObjectPropertyAssertion axiomTemplate) {
        super(axiomTemplate);
    }
    protected List<Atomic[]> addBindings(Reasoner reasoner, OWLDataFactory dataFactory, HermiTGraph graph, Atomic[] currentBinding, Map<Variable,Integer> bindingPositions) {
        Map<Variable,Atomic> bindingMap=new HashMap<Variable, Atomic>();
        // apply bindings that are already computed from previous steps
        for (Variable var : bindingPositions.keySet())
            bindingMap.put(var, currentBinding[bindingPositions.get(var)]);
        try {
            ObjectPropertyAssertion assertion=((ObjectPropertyAssertion)m_axiomTemplate.getBoundVersion(bindingMap)).getNormalizedAssertion();
            Atomic ope=(Atomic)assertion.getObjectPropertyExpression();
            Individual ind1=assertion.getIndividual1();
            Individual ind2=assertion.getIndividual2();
            if (ope.isVariable() && ind1.isVariable() && ind2.isVariable()) {
                int[] positions=new int[3];
                positions[0]=bindingPositions.get(ope);
                positions[1]=bindingPositions.get(ind1);
                positions[2]=bindingPositions.get(ind2);
                return compute012UnBound(reasoner,currentBinding,positions);
            } else if (ope.isVariable() && ind1.isVariable() && !ind2.isVariable()) {
                int[] positions=new int[2];
                positions[0]=bindingPositions.get(ope);
                positions[1]=bindingPositions.get(ind1);
                return compute2Bound(reasoner,currentBinding,(OWLNamedIndividual)ind2.asOWLAPIObject(dataFactory),positions);
            } else if (ope.isVariable() && !ind1.isVariable() && ind2.isVariable()) {
                int[] positions=new int[2];
                positions[0]=bindingPositions.get(ope);
                positions[1]=bindingPositions.get(ind2);
                return compute1Bound(reasoner,currentBinding,(OWLNamedIndividual)ind1.asOWLAPIObject(dataFactory),positions);
            } else if (ope.isVariable() && !ind1.isVariable() && !ind2.isVariable()) {
                int[] positions=new int[1];
                positions[0]=bindingPositions.get(ope);
                return compute12Bound(reasoner,currentBinding,(OWLNamedIndividual)ind1.asOWLAPIObject(dataFactory),(OWLNamedIndividual)ind2.asOWLAPIObject(dataFactory),positions);
            } else if (!ope.isVariable() && ind1.isVariable() && ind2.isVariable()) {
                int[] positions=new int[2];
                positions[0]=bindingPositions.get(ind1);
                positions[1]=bindingPositions.get(ind2);
                return compute0Bound(reasoner,currentBinding,(OWLObjectProperty)ope.asOWLAPIObject(dataFactory),positions);
            } else if (!ope.isVariable() && ind1.isVariable() && !ind2.isVariable()) {
                int[] positions=new int[1];
                positions[0]=bindingPositions.get(ind1);
                return compute02Bound(reasoner,currentBinding,(OWLObjectProperty)ope.asOWLAPIObject(dataFactory),(OWLNamedIndividual)ind2.asOWLAPIObject(dataFactory),positions);
            } else if (!ope.isVariable() && !ind1.isVariable() && !ind2.isVariable()) {
                return compute012Bound(reasoner,currentBinding,(OWLObjectProperty)ope.asOWLAPIObject(dataFactory),(OWLNamedIndividual)ind1.asOWLAPIObject(dataFactory),(OWLNamedIndividual)ind2.asOWLAPIObject(dataFactory));
            } else {
                return complex(reasoner,dataFactory,graph,currentBinding,assertion,bindingPositions);
            }
        } catch (IllegalArgumentException e) {
            // current binding is incompatible will not add new bindings in newBindings
            return new ArrayList<Atomic[]>();
        }
    }
    protected List<Atomic[]> compute012UnBound(Reasoner reasoner,Atomic[] currentBinding, int[] bindingPositions) {
        // ObjectPropertyAssertion(?x ?y ?z)
        List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
        for (OWLObjectProperty owlOP : reasoner.getRootOntology().getObjectPropertiesInSignature(true))
            newBindings.addAll(compute0Bound(reasoner, currentBinding, owlOP, bindingPositions));
        return newBindings;
    }
    protected List<Atomic[]> compute2Bound(Reasoner reasoner, Atomic[] currentBinding, OWLNamedIndividual ind, int[] bindingPositions) {
        // ObjectPropertyAssertion(?x ?y :a)
        List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
        for (OWLObjectProperty owlOP : reasoner.getRootOntology().getObjectPropertiesInSignature(true))
            newBindings.addAll(compute02Bound(reasoner, currentBinding, owlOP, ind, bindingPositions));
        return newBindings;
    }
    protected List<Atomic[]> compute1Bound(Reasoner reasoner, Atomic[] currentBinding, OWLNamedIndividual ind, int[] bindingPositions) {
        // ObjectPropertyAssertion(?x :a ?y)
        List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
        for (OWLObjectProperty owlOP : reasoner.getRootOntology().getObjectPropertiesInSignature(true))
            newBindings.addAll(compute01Bound(reasoner, currentBinding, owlOP, ind, bindingPositions));
        return newBindings;
    }
    protected List<Atomic[]> compute12Bound(Reasoner reasoner, Atomic[] currentBinding, OWLNamedIndividual ind1, OWLNamedIndividual ind2, int[] bindingPositions) {
        // ObjectPropertyAssertion(?x :a :b)
        List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
        for (OWLObjectProperty owlOP : reasoner.getRootOntology().getObjectPropertiesInSignature(true)) 
            newBindings.addAll(compute012Bound(reasoner, currentBinding, owlOP, ind1, ind2)); 
        return newBindings;
    }
    protected List<Atomic[]> compute0Bound(Reasoner reasoner, Atomic[] currentBinding, OWLObjectProperty ope, int[] bindingPositions) {
        // ObjectPropertyAssertion(:r ?x ?y)
        List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
        Map<OWLNamedIndividual,Set<OWLNamedIndividual>> instances=reasoner.getObjectPropertyInstances(ope);
        for (OWLNamedIndividual ind1 : instances.keySet())
            newBindings.addAll(compute01Bound(reasoner, currentBinding, ope, ind1, bindingPositions));
        return newBindings;
    }
    protected List<Atomic[]> compute02Bound(Reasoner reasoner, Atomic[] currentBinding, OWLObjectProperty ope, OWLNamedIndividual ind, int[] bindingPositions) {
        // ObjectPropertyAssertion(:r ?x :a)
        List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
        newBindings.addAll(compute01Bound(reasoner, currentBinding, ope.getInverseProperty(), ind, bindingPositions));
        return newBindings;
    }

    protected List<Atomic[]> compute01Bound(Reasoner reasoner, Atomic[] currentBinding, OWLObjectPropertyExpression ope, OWLNamedIndividual ind, int[] bindingPositions) {
        // ObjectPropertyAssertion(:r :a ?x)
        Atomic[] binding;
        List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
        NodeSet<OWLNamedIndividual> instances=reasoner.getObjectPropertyValues(ind, ope);
        for (OWLNamedIndividual relatedInd : instances.getFlattened()) {
            binding=currentBinding.clone();
            binding[bindingPositions[0]]=NamedIndividual.create(relatedInd.getIRI().toString());
            newBindings.add(binding);
        }
        return newBindings;
    }
    protected List<Atomic[]> compute012Bound(Reasoner reasoner, Atomic[] currentBinding, OWLObjectProperty ope, OWLNamedIndividual ind1, OWLNamedIndividual ind2) {
        // ObjectPropertyAssertion(:r :a :b)
        List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
        if (reasoner.hasObjectPropertyRelationship(ind1, ope, ind2))
            newBindings.add(currentBinding);
        return newBindings;
    }
    public int getCurrentCost(Reasoner reasoner, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions, HermiTGraph graph) {
        int cost=0;
        if (candidateBindings.isEmpty())
            return cost; // no answers, no tests
        OWLDataFactory dataFactory=reasoner.getDataFactory();
        for (Atomic[] testBinding : candidateBindings) { 
            Set<Variable> opVars=m_axiomTemplate.getObjectPropertyExpression().getVariablesInSignature();
            Set<Variable> indVars=m_axiomTemplate.getVariablesInSignature();
            Map<Variable,Atomic> existingBindings=new HashMap<Variable,Atomic>();
            for (Variable var : indVars) {
                Atomic binding=testBinding[bindingPositions.get(var)];
                if (binding!=null)
                    existingBindings.put(var,binding);
            }
            Set<Variable> unbound=new HashSet<Variable>(indVars);
            indVars.removeAll(opVars);
            Variable opVar=opVars.isEmpty()?null:opVars.iterator().next();
            unbound.removeAll(existingBindings.keySet());
            ObjectPropertyAssertion normalized=m_axiomTemplate.getNormalizedAssertion();
            if (unbound.size()==0)
                return candidateBindings.size()*COST_LOOKUP; // check entailment for each candidate binding, might have to really test, not just lookup
            else if (unbound.size()==2) {
                if (opVar!=null) {
                    // r(?x ?y)
                    ObjectProperty op=(ObjectProperty)normalized.getObjectPropertyExpression();
                    int[] estimate=reasoner.getNumberOfInstances((OWLObjectProperty)op.getBoundVersion(existingBindings,dataFactory));
                    cost+=estimate[0]*COST_LOOKUP;
                    cost+=estimate[1]*COST_ENTAILMENT; // although we might have to do several tests
                } else if (unbound.contains(normalized.getIndividual2())) {
                    // ?x(:a ?y)
                    for (ObjectProperty op : graph.getObjectPropertiesInSignature()) {
                        int[] estimate=reasoner.getNumberOfSuccessors((OWLObjectProperty)op.getBoundVersion(existingBindings,dataFactory), (OWLNamedIndividual)normalized.getIndividual1().getBoundVersion(existingBindings,dataFactory));
                        cost+=estimate[0]*COST_LOOKUP;
                        cost+=estimate[1]*COST_ENTAILMENT; // although we might have to do several tests
                    }
                } else {
                    // ?x(?y :a)
                    for (ObjectProperty op : graph.getObjectPropertiesInSignature()) {
                        int[] estimate=reasoner.getNumberOfPredecessors((OWLObjectProperty)op.getBoundVersion(existingBindings,dataFactory), (OWLNamedIndividual)normalized.getIndividual2().getBoundVersion(existingBindings,dataFactory));
                        cost+=estimate[0]*COST_LOOKUP;
                        cost+=estimate[1]*COST_ENTAILMENT; // although we might have to do several tests
                    }
                }
            } else if (unbound.size()==1) {
                if (opVar==null) {
                    // ?x(:a :b)
                    cost+=(graph.getObjectPropertiesInSignature().size()*COST_ENTAILMENT); // might just do lookup though
                } else if (unbound.contains(normalized.getIndividual1())) {
                    // r(?x :a)
                    int[] estimate=reasoner.getNumberOfPredecessors((OWLObjectProperty)normalized.getObjectPropertyExpression().getBoundVersion(existingBindings, dataFactory), (OWLNamedIndividual)normalized.getIndividual2().getBoundVersion(existingBindings,dataFactory));
                    cost+=estimate[0]*COST_LOOKUP;
                    cost+=estimate[1]*COST_ENTAILMENT; // although we might have to do several tests
                }
            } else {
                // ?x(?y ?z)
                for (ObjectProperty op : graph.getObjectPropertiesInSignature()) {
                    int[] estimate=reasoner.getNumberOfInstances((OWLObjectProperty)op.getBoundVersion(existingBindings,dataFactory));
                    cost+=estimate[0]*COST_LOOKUP;
                    cost+=estimate[1]*COST_ENTAILMENT; // although we might have to do several tests
                }
            }
        }
        return cost;
    }
}
