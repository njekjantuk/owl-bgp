package org.semanticweb.sparql.evaluation.queryobjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.sparql.arq.HermiTGraph;
import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.Variable;
import org.semanticweb.sparql.owlbgp.model.axioms.ClassAssertion;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.classexpressions.Clazz;
import org.semanticweb.sparql.owlbgp.model.individuals.Individual;
import org.semanticweb.sparql.owlbgp.model.individuals.NamedIndividual;

public class QO_ClassAssertion extends AbstractQueryObject<ClassAssertion> {

    public QO_ClassAssertion(ClassAssertion axiomTemplate) {
	    super(axiomTemplate);
	}
	protected List<Atomic[]> addBindings(Reasoner reasoner, OWLDataFactory dataFactory, HermiTGraph graph, Atomic[] currentBinding, Map<Variable,Integer> bindingPositions) {
		Map<Variable,Atomic> bindingMap=new HashMap<Variable, Atomic>();
		// apply bindings that are already computed from previous steps
		for (Variable var : bindingPositions.keySet())
		    bindingMap.put(var, currentBinding[bindingPositions.get(var)]);
		try {
    		ClassAssertion assertion=(ClassAssertion)m_axiomTemplate.getBoundVersion(bindingMap);
    		ClassExpression ce=assertion.getClassExpression();
    		Individual ind=assertion.getIndividual();
            if (ce.isVariable() && ind.isVariable()) {
                int[] positions=new int[2];
                positions[0]=bindingPositions.get(ce);
                positions[1]=bindingPositions.get(ind);
                return computeAllClassAssertions(reasoner,currentBinding,positions);
            } else if (ce.isVariable() && !ind.isVariable()) {
                int position=bindingPositions.get(ce);
                return computeTypes(reasoner,currentBinding,(OWLNamedIndividual)ind.asOWLAPIObject(dataFactory),position);
            } else if (!ce.isVariable() && ind.isVariable()) {
                int position=bindingPositions.get(ind);
                return computeInstances(reasoner,currentBinding,(OWLClassExpression)ce.asOWLAPIObject(dataFactory),position);
            } else if (!ce.isVariable() && !ind.isVariable()) {
                return checkType(reasoner,currentBinding,(OWLClassExpression)ce.asOWLAPIObject(dataFactory),(OWLNamedIndividual)ind.asOWLAPIObject(reasoner.getDataFactory()));
            } else {
                return complex(reasoner, dataFactory, graph, currentBinding, assertion, bindingPositions);
            }
		} catch (IllegalArgumentException e) {
		    // current binding is incompatible will not add new bindings in newBindings
		    return new ArrayList<Atomic[]>();
		}
	}
    protected List<Atomic[]> checkType(Reasoner reasoner, Atomic[] currentBinding, OWLClassExpression classExpression, OWLNamedIndividual individual) {
        // ClassAssertion(:C :a)
	    List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
        if (reasoner.hasType(individual, classExpression, false)) 
            newBindings.add(currentBinding);
        return newBindings;
    }
	protected List<Atomic[]> computeTypes(Reasoner reasoner, Atomic[] currentBinding, OWLNamedIndividual individual, int bindingPosition) {
        // ClassAssertion(?x :a)
        Atomic[] binding;
        List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
        Set<OWLClass> types=reasoner.getTypes(individual, false).getFlattened();
        for (OWLClass type : types) {
            binding=currentBinding.clone();
            binding[bindingPosition]=Clazz.create(type.getIRI().toString());
            newBindings.add(binding);
        }
        return newBindings;
    }
    protected List<Atomic[]> computeInstances(Reasoner reasoner, Atomic[] currentBinding, OWLClassExpression classExpression, int bindingPosition) {
        // ClassAssertion(:C ?y)
        Atomic[] binding;
        List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
        Set<OWLNamedIndividual> instances=reasoner.getInstances(classExpression, false).getFlattened();
        for (OWLNamedIndividual instance : instances) {
            binding=currentBinding.clone();
            binding[bindingPosition]=NamedIndividual.create(instance.getIRI().toString());
            newBindings.add(binding);
        }
        return newBindings;
    }
    protected List<Atomic[]> computeAllClassAssertions(Reasoner reasoner,Atomic[] currentBinding, int[] bindingPositions) {
        // ClassAssertion(?x ?y)
        Atomic[] binding;
        List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
        for (OWLClass owlClass : reasoner.getRootOntology().getClassesInSignature(true)) {
            NodeSet<OWLNamedIndividual> instances=reasoner.getInstances(owlClass, false);
            for (OWLNamedIndividual ind : instances.getFlattened()) {
                binding=currentBinding.clone();
                binding[bindingPositions[0]]=Clazz.create(owlClass.getIRI().toString());
                binding[bindingPositions[1]]=NamedIndividual.create(ind.getIRI().toString());
                newBindings.add(binding);
            }
        }
        return newBindings;
    }
    public int getCurrentCost(Reasoner reasoner, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions, HermiTGraph graph) {
        int cost=0;
        if (candidateBindings.isEmpty())
            return cost; // no answers, no tests
        OWLDataFactory dataFactory=reasoner.getDataFactory();
        for (Atomic[] testBinding : candidateBindings) { 
            Set<Variable> classVars=m_axiomTemplate.getClassExpression().getVariablesInSignature();
            Set<Variable> indVars=m_axiomTemplate.getIndividual().getVariablesInSignature();
            Map<Variable,Atomic> existingBindings=new HashMap<Variable,Atomic>();
            for (Variable var : classVars) {
                Atomic binding=testBinding[bindingPositions.get(var)];
                if (binding!=null)
                    existingBindings.put(var,binding);
            }
            Variable indVar=indVars.isEmpty()?null:indVars.iterator().next();
            Set<Variable> unbound=new HashSet<Variable>(classVars);
            unbound.removeAll(existingBindings.keySet());
            if (indVar!=null && !existingBindings.containsKey(indVar))
                unbound.add(indVar);
            
            if (unbound.size()==0)
                if (m_axiomTemplate.getClassExpression() instanceof Atomic)
                    return candidateBindings.size()*COST_LOOKUP; // check entailment for each candidate binding, might have to really test, not just lookup
                else 
                    return candidateBindings.size() * COST_CLASS_HIERARCHY_INSERTION;
            else if (unbound.size()==1 && indVar!=null) {
                // C(?x)
                ClassExpression ce=m_axiomTemplate.getClassExpression();
                if (ce instanceof Atomic) {
                    int[] estimate=reasoner.getNumberOfInstances((OWLClass)ce.getBoundVersion(existingBindings,dataFactory));
                    cost+=estimate[0]*COST_LOOKUP;
                    cost+=estimate[1]*COST_ENTAILMENT; // although we might have to do several tests
                } else {
                    // CE(?x)
                    return candidateBindings.size()*COST_CLASS_HIERARCHY_INSERTION; // although we might have to do several tests then for the possible ones
                }
            } else if (indVar==null && classVars.size()==1 && testBinding[bindingPositions.get(classVars.iterator().next())] instanceof Atomic) {
                // ?x(:a)
                Individual ind=m_axiomTemplate.getIndividual();
                int[] estimate=reasoner.getNumberOfTypes((OWLNamedIndividual)ind.asOWLAPIObject(dataFactory));
                cost+=estimate[0]*COST_LOOKUP;
                cost+=estimate[1]*COST_ENTAILMENT; // although we might have to do several tests
            } else
                return complexCost(candidateBindings, bindingPositions, graph, unbound);
        }
        return cost;
    }
}
