package org.semanticweb.sparql.bgpevaluation.queryobjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.sparql.arq.OWLOntologyGraph;
import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.Variable;
import org.semanticweb.sparql.owlbgp.model.axioms.ObjectPropertyAssertion;
import org.semanticweb.sparql.owlbgp.model.individuals.Individual;
import org.semanticweb.sparql.owlbgp.model.individuals.NamedIndividual;

public class QO_ObjectPropertyAssertion  extends AbstractQueryObject<ObjectPropertyAssertion> {

    public QO_ObjectPropertyAssertion(ObjectPropertyAssertion axiomTemplate) {
        super(axiomTemplate);
    }
    protected List<Atomic[]> addBindings(OWLReasoner reasoner, OWLDataFactory dataFactory, OWLOntologyGraph graph, Atomic[] currentBinding, Map<Variable,Integer> bindingPositions) {
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
    protected List<Atomic[]> compute012UnBound(OWLReasoner reasoner,Atomic[] currentBinding, int[] bindingPositions) {
        // ObjectPropertyAssertion(?x ?y ?z)
        List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
        for (OWLObjectProperty owlOP : reasoner.getRootOntology().getObjectPropertiesInSignature(true))
            newBindings.addAll(compute0Bound(reasoner, currentBinding, owlOP, bindingPositions));
        return newBindings;
    }
    protected List<Atomic[]> compute2Bound(OWLReasoner reasoner, Atomic[] currentBinding, OWLNamedIndividual ind, int[] bindingPositions) {
        // ObjectPropertyAssertion(?x ?y :a)
        List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
        for (OWLObjectProperty owlOP : reasoner.getRootOntology().getObjectPropertiesInSignature(true))
            newBindings.addAll(compute02Bound(reasoner, currentBinding, owlOP, ind, bindingPositions));
        return newBindings;
    }
    protected List<Atomic[]> compute1Bound(OWLReasoner reasoner, Atomic[] currentBinding, OWLNamedIndividual ind, int[] bindingPositions) {
        // ObjectPropertyAssertion(?x :a ?y)
        List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
        for (OWLObjectProperty owlOP : reasoner.getRootOntology().getObjectPropertiesInSignature(true))
            newBindings.addAll(compute01Bound(reasoner, currentBinding, owlOP, ind, bindingPositions));
        return newBindings;
    }
    protected List<Atomic[]> compute12Bound(OWLReasoner reasoner, Atomic[] currentBinding, OWLNamedIndividual ind1, OWLNamedIndividual ind2, int[] bindingPositions) {
        // ObjectPropertyAssertion(?x :a :b)
        List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
        for (OWLObjectProperty owlOP : reasoner.getRootOntology().getObjectPropertiesInSignature(true)) 
            newBindings.addAll(compute012Bound(reasoner, currentBinding, owlOP, ind1, ind2)); 
        return newBindings;
    }
    protected List<Atomic[]> compute0Bound(OWLReasoner reasoner, Atomic[] currentBinding, OWLObjectProperty ope, int[] bindingPositions) {
        // ObjectPropertyAssertion(:r ?x ?y)
        List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
        Atomic[] binding;
        if (reasoner instanceof Reasoner) {
            Map<OWLNamedIndividual,Set<OWLNamedIndividual>> instances=((Reasoner)reasoner).getObjectPropertyInstances(ope);
            for (OWLNamedIndividual ind1 : instances.keySet())
                for (OWLNamedIndividual ind2 : instances.get(ind1)) {
                    binding=currentBinding.clone();
                    binding[bindingPositions[0]]=NamedIndividual.create(ind1.getIRI().toString());
                    binding[bindingPositions[1]]=NamedIndividual.create(ind2.getIRI().toString());
                    newBindings.add(binding);
                }
        } else {
            for (OWLNamedIndividual ind1 : reasoner.getRootOntology().getIndividualsInSignature(true)) {
                NodeSet<OWLNamedIndividual> instances=reasoner.getObjectPropertyValues(ind1, ope);
                for (OWLNamedIndividual ind2 : instances.getFlattened()) {
                    binding=currentBinding.clone();
                    binding[bindingPositions[0]]=NamedIndividual.create(ind1.getIRI().toString());
                    binding[bindingPositions[1]]=NamedIndividual.create(ind2.getIRI().toString());
                    newBindings.add(binding);
                }
            }
        }
        return newBindings;
    }
    protected List<Atomic[]> compute02Bound(OWLReasoner reasoner, Atomic[] currentBinding, OWLObjectProperty ope, OWLNamedIndividual ind, int[] bindingPositions) {
        // ObjectPropertyAssertion(:r ?x :a)
        List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
        newBindings.addAll(compute01Bound(reasoner, currentBinding, ope.getInverseProperty(), ind, bindingPositions));
        return newBindings;
    }

    protected List<Atomic[]> compute01Bound(OWLReasoner reasoner, Atomic[] currentBinding, OWLObjectPropertyExpression ope, OWLNamedIndividual ind, int[] bindingPositions) {
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
    protected List<Atomic[]> compute012Bound(OWLReasoner reasoner, Atomic[] currentBinding, OWLObjectProperty ope, OWLNamedIndividual ind1, OWLNamedIndividual ind2) {
        // ObjectPropertyAssertion(:r :a :b)
        List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
        if (reasoner.getObjectPropertyValues(ind1, ope).containsEntity(ind2))
            newBindings.add(currentBinding);
        return newBindings;
    }
}
