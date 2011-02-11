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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.sparql.arq.OWLOntologyGraph;
import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.FromOWLAPIConverter;
import org.semanticweb.sparql.owlbgp.model.Variable;
import org.semanticweb.sparql.owlbgp.model.axioms.SubClassOf;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.classexpressions.Clazz;

public class QO_SubClassOf extends AbstractQueryObject<SubClassOf> {

	public QO_SubClassOf(SubClassOf axiomTemplate, OWLOntologyGraph graph) {
        super(axiomTemplate, graph);
    }
	
    protected List<Atomic[]> addBindings(Atomic[] currentBinding, Map<Variable,Integer> bindingPositions) {
	    Map<Variable,Atomic> bindingMap=new HashMap<Variable, Atomic>();
        // apply bindings that are already computed from previous steps
        for (Variable var : bindingPositions.keySet())
            bindingMap.put(var, currentBinding[bindingPositions.get(var)]);
        try {
            SubClassOf axiom=(SubClassOf)m_axiomTemplate.getBoundVersion(bindingMap);
            ClassExpression subClass=axiom.getSubClassExpression();
            ClassExpression superClass=axiom.getSuperClassExpression();
            if (subClass.isVariable() && superClass.isVariable()) {
                int[] positions=new int[2];
                positions[0]=bindingPositions.get(subClass);
                positions[1]=bindingPositions.get(superClass);
                return computeAllSubClassOfRelations(currentBinding,positions);
            } else if (subClass.isVariable() && !superClass.isVariable()) {
                int position=bindingPositions.get(subClass);
                return computeSubClasses(currentBinding,(OWLClassExpression)superClass.asOWLAPIObject(m_toOWLAPIConverter),position);
            } else if (!subClass.isVariable() && superClass.isVariable()) {
                int position=bindingPositions.get(superClass);
                return computeSuperClasses(currentBinding,(OWLClassExpression)subClass.asOWLAPIObject(m_toOWLAPIConverter),position);
            } else if (!subClass.isVariable() && !superClass.isVariable()) {
                return checkSubsumption(currentBinding,(OWLClassExpression)subClass.asOWLAPIObject(m_toOWLAPIConverter),(OWLClassExpression)superClass.asOWLAPIObject(m_toOWLAPIConverter));
            } else {
                return complex(currentBinding,axiom,bindingPositions);
            }
        } catch (IllegalArgumentException e) {
            // current binding is incompatible will not add new bindings in newBindings
            return new ArrayList<Atomic[]>();
        }
	}
	protected List<Atomic[]> computeAllSubClassOfRelations(Atomic[] currentBinding, int[] bindingPositions) {
        // SubClassOf(?x ?y)
	    Atomic[] binding;
        List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
        for (Clazz clazz : m_graph.getClassesInSignature()) {
            OWLClass owlClass=(OWLClass)clazz.asOWLAPIObject(m_toOWLAPIConverter);
            Set<OWLClass> superClasses=m_reasoner.getSuperClasses(owlClass,false).getFlattened();
            superClasses.addAll(m_reasoner.getEquivalentClasses(owlClass).getEntities());
            for (OWLClass cls : superClasses) {
                binding=currentBinding.clone();
                binding[bindingPositions[0]]=clazz;
                binding[bindingPositions[1]]=(Clazz)FromOWLAPIConverter.convert(cls);
                newBindings.add(binding);
            }
        }
        return newBindings;
	}
    protected List<Atomic[]> computeSubClasses(Atomic[] currentBinding, OWLClassExpression superClass, int bindingPosition) {
         // SubClassOf(?x :C)
	     Atomic[] binding;
	     List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
         Set<OWLClass> subs=m_reasoner.getSubClasses(superClass,false).getFlattened();
         subs.addAll(m_reasoner.getEquivalentClasses(superClass).getEntities());
         for (OWLClass sub : subs) {
             binding=currentBinding.clone();
             binding[bindingPosition]=(Clazz)FromOWLAPIConverter.convert(sub);
             newBindings.add(binding);
         }
	     return newBindings;
	 }
    protected List<Atomic[]> computeSuperClasses(Atomic[] currentBinding, OWLClassExpression subClass, int bindingPosition) {
        // SubClassOf(?x :C)
        Atomic[] binding;
        List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
        Set<OWLClass> sups=m_reasoner.getSuperClasses(subClass,false).getFlattened();
        sups.addAll(m_reasoner.getEquivalentClasses(subClass).getEntities());
        for (OWLClass sup : sups) {
            binding=currentBinding.clone();
            binding[bindingPosition]=(Clazz)FromOWLAPIConverter.convert(sup);
            newBindings.add(binding);
        }
        return newBindings;
    }
    protected List<Atomic[]> checkSubsumption(Atomic[] currentBinding, OWLClassExpression subClass, OWLClassExpression superClass) {
        // SubClassOf(:C :D)
        List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
        if (m_reasoner.isEntailed(m_dataFactory.getOWLSubClassOfAxiom(subClass, superClass)))
            newBindings.add(currentBinding);
        return newBindings;
    }
    public <O> O accept(QueryObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
}
