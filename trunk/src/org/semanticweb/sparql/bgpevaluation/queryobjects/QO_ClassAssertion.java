/* Copyright 2011 by the Oxford University Computing Laboratory

   This file is part of OWL-BGP.

   OWL-BGP is free software: you can redistribute it and/or modify
   it under the terms of the GNU Lesser General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   OWL-BGP is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU Lesser General Public License for more details.

   You should have received a copy of the GNU Lesser General Public License
   along with OWL-BGP. If not, see <http://www.gnu.org/licenses/>.
 */

package  org.semanticweb.sparql.bgpevaluation.queryobjects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.sparql.arq.OWLOntologyGraph;
import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.Variable;
import org.semanticweb.sparql.owlbgp.model.axioms.ClassAssertion;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.classexpressions.Clazz;
import org.semanticweb.sparql.owlbgp.model.individuals.Individual;
import org.semanticweb.sparql.owlbgp.model.individuals.NamedIndividual;

public class QO_ClassAssertion extends AbstractQueryObject<ClassAssertion> {

	public QO_ClassAssertion(ClassAssertion axiomTemplate, OWLOntologyGraph graph) {
        super(axiomTemplate, graph);
    }
	
    protected List<Atomic[]> addBindings(Atomic[] currentBinding, Map<Variable,Integer> bindingPositions) {
		Map<Variable,Atomic> bindingMap=new HashMap<Variable, Atomic>();
		// apply bindings that are already computed from previous steps
		for (Variable var : bindingPositions.keySet())
		    bindingMap.put(var, currentBinding[bindingPositions.get(var)]);
		try {
    		ClassAssertion instantiated=(ClassAssertion)m_axiomTemplate.getBoundVersion(bindingMap);
    		System.out.println(instantiated);
    		ClassExpression ce=instantiated.getClassExpression();
    		Set<Variable> ceVars=ce.getVariablesInSignature();
    		Individual ind=instantiated.getIndividual();
            if (ce.isVariable() && ind.isVariable()) {
                int[] positions=new int[2];
                positions[0]=bindingPositions.get(ce);
                positions[1]=bindingPositions.get(ind);
                return computeAllClassAssertions(currentBinding,positions);
            } else if (ce.isVariable() && !ind.isVariable()) {
                int position=bindingPositions.get(ce);
                return computeTypes(currentBinding,(OWLNamedIndividual)ind.asOWLAPIObject(m_dataFactory),position);
            } else if (ceVars.isEmpty() && ind.isVariable()) {
                int position=bindingPositions.get(ind);
                return computeInstances(currentBinding,(OWLClassExpression)ce.asOWLAPIObject(m_dataFactory),position);
            } else if (ceVars.isEmpty() && !ind.isVariable()) {
                if (checkType((OWLClassExpression)ce.asOWLAPIObject(m_dataFactory),(OWLNamedIndividual)ind.asOWLAPIObject(m_dataFactory)))
                    return Collections.singletonList(currentBinding);
                else 
                    return new ArrayList<Atomic[]>();
            } else {
                return complex(currentBinding, instantiated, bindingPositions);
            }
		} catch (IllegalArgumentException e) {
		    // current binding is incompatible will not add new bindings in newBindings
		    return new ArrayList<Atomic[]>();
		}
	}
    protected boolean checkType(OWLClassExpression classExpression, OWLNamedIndividual individual) {
        // ClassAssertion(:C :a)
        if (m_reasoner instanceof Reasoner)
            return ((Reasoner)m_reasoner).hasType(individual, classExpression, false);
        else
    	    return m_reasoner.getInstances(classExpression, false).containsEntity(individual); 
    }
	protected List<Atomic[]> computeTypes(Atomic[] currentBinding, OWLNamedIndividual individual, int bindingPosition) {
        // ClassAssertion(?x :a)
        Atomic[] binding;
        List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
        Set<OWLClass> types=m_reasoner.getTypes(individual, false).getFlattened();
        for (OWLClass type : types) {
            binding=currentBinding.clone();
            binding[bindingPosition]=Clazz.create(type.getIRI().toString());
            newBindings.add(binding);
        }
        return newBindings;
    }
    protected List<Atomic[]> computeInstances(Atomic[] currentBinding, OWLClassExpression classExpression, int bindingPosition) {
        // ClassAssertion(:C ?y)
        Atomic[] binding;
        List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
        Set<OWLNamedIndividual> instances=m_reasoner.getInstances(classExpression, false).getFlattened();
        for (OWLNamedIndividual instance : instances) {
            binding=currentBinding.clone();
            binding[bindingPosition]=NamedIndividual.create(instance.getIRI().toString());
            newBindings.add(binding);
        }
        return newBindings;
    }
    protected List<Atomic[]> computeAllClassAssertions(Atomic[] currentBinding, int[] bindingPositions) {
        // ClassAssertion(?x ?y)
        Atomic[] binding;
        List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
        for (OWLClass owlClass : m_reasoner.getRootOntology().getClassesInSignature(true)) {
            NodeSet<OWLNamedIndividual> instances=m_reasoner.getInstances(owlClass, false);
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
