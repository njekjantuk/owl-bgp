package org.semanticweb.sparql.bgpevaluation.queryobjects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.sparql.arq.OWLOntologyGraph;
import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.FromOWLAPIConverter;
import org.semanticweb.sparql.owlbgp.model.Variable;
import org.semanticweb.sparql.owlbgp.model.axioms.SubClassOf;
import org.semanticweb.sparql.owlbgp.model.axioms.SubObjectPropertyOf;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectProperty;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;

public class QO_SubObjectPropertyOf extends AbstractQueryObject<SubClassOf> {

	public QO_SubObjectPropertyOf(SubClassOf axiomTemplate, OWLOntologyGraph graph) {
        super(axiomTemplate, graph);
    }
	
    protected List<Atomic[]> addBindings(Atomic[] currentBinding, Map<Variable,Integer> bindingPositions) {
	    Map<Variable,Atomic> bindingMap=new HashMap<Variable, Atomic>();
        // apply bindings that are already computed from previous steps
        for (Variable var : bindingPositions.keySet())
            bindingMap.put(var, currentBinding[bindingPositions.get(var)]);
        try {
            SubObjectPropertyOf axiom=(SubObjectPropertyOf)m_axiomTemplate.getBoundVersion(bindingMap);
            ObjectPropertyExpression subOPE=axiom.getSubObjectPropertyExpression();
            ObjectPropertyExpression superOPE=axiom.getSuperObjectPropertyExpression();
            if (subOPE.isVariable() && superOPE.isVariable()) {
                int[] positions=new int[2];
                positions[0]=bindingPositions.get(subOPE);
                positions[1]=bindingPositions.get(superOPE);
                return computeAllSubObjectPropertyOfRelations(currentBinding,positions);
            } else if (subOPE.isVariable() && !superOPE.isVariable()) {
                int position=bindingPositions.get(subOPE);
                return computeSubObjectProperties(currentBinding,(OWLObjectProperty)superOPE.asOWLAPIObject(m_toOWLAPIConverter),position);
            } else if (!subOPE.isVariable() && superOPE.isVariable()) {
                int position=bindingPositions.get(superOPE);
                return computeSuperObjectProperties(currentBinding,(OWLObjectProperty)subOPE.asOWLAPIObject(m_toOWLAPIConverter),position);
            } else if (!subOPE.isVariable() && !superOPE.isVariable()) {
                if (checkSubsumption(currentBinding,(OWLObjectProperty)subOPE.asOWLAPIObject(m_toOWLAPIConverter),(OWLObjectProperty)superOPE.asOWLAPIObject(m_toOWLAPIConverter)))
                    return Collections.singletonList(currentBinding);
                else 
                    return new ArrayList<Atomic[]>();
            } else {
                return complex(currentBinding,axiom,bindingPositions);
            }
        } catch (IllegalArgumentException e) {
            // current binding is incompatible will not add new bindings in newBindings
            return new ArrayList<Atomic[]>();
        }
	}
	protected List<Atomic[]> computeAllSubObjectPropertyOfRelations(Atomic[] currentBinding, int[] bindingPositions) {
        // SubObjectPropertyOf(?x ?y)
	    Atomic[] binding;
        List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
        for (ObjectProperty op : m_graph.getObjectPropertiesInSignature()) {
            OWLObjectProperty owlOP=(OWLObjectProperty)op.asOWLAPIObject(m_toOWLAPIConverter);
            Set<OWLObjectPropertyExpression> sups=m_reasoner.getSuperObjectProperties(owlOP,false).getFlattened();
            sups.addAll(m_reasoner.getEquivalentObjectProperties(owlOP).getEntities());
            for (OWLObjectPropertyExpression sup : sups) {
                if (!sup.isAnonymous()) {
                    binding=currentBinding.clone();
                    binding[bindingPositions[0]]=op;
                    binding[bindingPositions[1]]=(ObjectProperty)FromOWLAPIConverter.convert(sup);
                    newBindings.add(binding);
                }
            }
        }
        return newBindings;
	}
    protected List<Atomic[]> computeSubObjectProperties(Atomic[] currentBinding, OWLObjectProperty sup, int bindingPosition) {
         // SubObjectPropertyOf(?x :C)
	     Atomic[] binding;
	     List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
         Set<OWLObjectPropertyExpression> subs=m_reasoner.getSubObjectProperties(sup,false).getFlattened();
         subs.addAll(m_reasoner.getEquivalentObjectProperties(sup).getEntities());
         for (OWLObjectPropertyExpression sub : subs) {
             if (!sub.isAnonymous()) {
                 binding=currentBinding.clone();
                 binding[bindingPosition]=(ObjectProperty)FromOWLAPIConverter.convert(sub);
                 newBindings.add(binding);
             }
         }
	     return newBindings;
	 }
    protected List<Atomic[]> computeSuperObjectProperties(Atomic[] currentBinding, OWLObjectProperty sub, int bindingPosition) {
        // SubObjectPropertyOf(?x :C)
        Atomic[] binding;
        List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
        Set<OWLObjectPropertyExpression> sups=m_reasoner.getSuperObjectProperties(sub,false).getFlattened();
        sups.addAll(m_reasoner.getEquivalentObjectProperties(sub).getEntities());
        for (OWLObjectPropertyExpression sup : sups) {
            if (!sup.isAnonymous()) {
                binding=currentBinding.clone();
                binding[bindingPosition]=(ObjectProperty)FromOWLAPIConverter.convert(sup);
                newBindings.add(binding);
            }
        }
        return newBindings;
    }
    protected boolean checkSubsumption(Atomic[] currentBinding, OWLObjectProperty sub, OWLObjectProperty sup) {
        // SubObjectPropertyOf(:C :D)
        return m_reasoner.isEntailed(m_dataFactory.getOWLSubObjectPropertyOfAxiom(sub, sup));
    }
    public <O> O accept(QueryObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
}
