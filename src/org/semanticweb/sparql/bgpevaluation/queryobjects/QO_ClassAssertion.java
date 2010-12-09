package org.semanticweb.sparql.bgpevaluation.queryobjects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.sparql.arq.OWLOntologyGraph;
import org.semanticweb.sparql.bgpevaluation.QueryObjectVisitorEx;
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
	protected List<Atomic[]> addBindings(OWLReasoner reasoner, OWLDataFactory dataFactory, OWLOntologyGraph graph, Atomic[] currentBinding, Map<Variable,Integer> bindingPositions) {
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
                if (checkType(reasoner,currentBinding,(OWLClassExpression)ce.asOWLAPIObject(dataFactory),(OWLNamedIndividual)ind.asOWLAPIObject(dataFactory)))
                    return Collections.singletonList(currentBinding);
                else 
                    return new ArrayList<Atomic[]>();
            } else {
                return complex(reasoner, dataFactory, graph, currentBinding, assertion, bindingPositions);
            }
		} catch (IllegalArgumentException e) {
		    // current binding is incompatible will not add new bindings in newBindings
		    return new ArrayList<Atomic[]>();
		}
	}
    protected boolean checkType(OWLReasoner reasoner, Atomic[] currentBinding, OWLClassExpression classExpression, OWLNamedIndividual individual) {
        // ClassAssertion(:C :a)
        if (reasoner instanceof Reasoner)
            return ((Reasoner)reasoner).hasType(individual, classExpression, false);
        else
    	    return reasoner.getInstances(classExpression, false).containsEntity(individual); 
    }
	protected List<Atomic[]> computeTypes(OWLReasoner reasoner, Atomic[] currentBinding, OWLNamedIndividual individual, int bindingPosition) {
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
    protected List<Atomic[]> computeInstances(OWLReasoner reasoner, Atomic[] currentBinding, OWLClassExpression classExpression, int bindingPosition) {
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
    protected List<Atomic[]> computeAllClassAssertions(OWLReasoner reasoner,Atomic[] currentBinding, int[] bindingPositions) {
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
    public <O> O accept(QueryObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
}
