package org.semanticweb.sparql.bgpevaluation.queryobjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.sparql.arq.OWLOntologyGraph;
import org.semanticweb.sparql.bgpevaluation.QueryObjectVisitorEx;
import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.Variable;
import org.semanticweb.sparql.owlbgp.model.axioms.SubClassOf;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.classexpressions.Clazz;

public class QO_SubClassOf extends AbstractQueryObject<SubClassOf> {
	
	public QO_SubClassOf(SubClassOf axiomTemplateTemplate) {
	    super(axiomTemplateTemplate);
	}
	protected List<Atomic[]> addBindings(OWLReasoner reasoner, OWLDataFactory dataFactory, OWLOntologyGraph graph, Atomic[] currentBinding, Map<Variable,Integer> bindingPositions) {
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
                return computeAllSubClassOfRelations(reasoner,currentBinding,positions);
            } else if (subClass.isVariable() && !superClass.isVariable()) {
                int position=bindingPositions.get(subClass);
                return computeSubClasses(reasoner,currentBinding,(OWLClassExpression)superClass.asOWLAPIObject(dataFactory),position);
            } else if (!subClass.isVariable() && superClass.isVariable()) {
                int position=bindingPositions.get(superClass);
                return computeSuperClasses(reasoner,currentBinding,(OWLClassExpression)subClass.asOWLAPIObject(dataFactory),position);
            } else if (!subClass.isVariable() && !superClass.isVariable()) {
                return checkSubsumption(reasoner,dataFactory,currentBinding,(OWLClassExpression)subClass.asOWLAPIObject(dataFactory),(OWLClassExpression)superClass.asOWLAPIObject(dataFactory));
            } else {
                return complex(reasoner,dataFactory,graph,currentBinding,axiom,bindingPositions);
            }
        } catch (IllegalArgumentException e) {
            // current binding is incompatible will not add new bindings in newBindings
            return new ArrayList<Atomic[]>();
        }
	}
	protected List<Atomic[]> computeAllSubClassOfRelations(OWLReasoner reasoner, Atomic[] currentBinding, int[] bindingPositions) {
        // SubClassOf(?x ?y)
	    Atomic[] binding;
        List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
        for (OWLClass owlClass : reasoner.getRootOntology().getClassesInSignature(true)) {
            Set<OWLClass> superClasses=reasoner.getSuperClasses(owlClass,false).getFlattened();
            superClasses.addAll(reasoner.getEquivalentClasses(owlClass).getEntities());
            for (OWLClass cls : superClasses) {
                binding=currentBinding.clone();
                binding[bindingPositions[0]]=Clazz.create(owlClass.getIRI().toString());
                binding[bindingPositions[1]]=Clazz.create(cls.getIRI().toString());
                newBindings.add(binding);
            }
        }
        return newBindings;
	}
    protected List<Atomic[]> computeSubClasses(OWLReasoner reasoner, Atomic[] currentBinding, OWLClassExpression superClass, int bindingPosition) {
         // SubClassOf(?x :C)
	     Atomic[] binding;
	     List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
         Set<OWLClass> subs=reasoner.getSubClasses(superClass,false).getFlattened();
         subs.addAll(reasoner.getEquivalentClasses(superClass).getEntities());
         for (OWLClass sub : subs) {
             binding=currentBinding.clone();
             binding[bindingPosition]=Clazz.create(sub.getIRI().toString());
             newBindings.add(binding);
         }
	     return newBindings;
	 }
    protected List<Atomic[]> computeSuperClasses(OWLReasoner reasoner, Atomic[] currentBinding, OWLClassExpression subClass, int bindingPosition) {
        // SubClassOf(?x :C)
        Atomic[] binding;
        List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
        Set<OWLClass> sups=reasoner.getSuperClasses(subClass,false).getFlattened();
        sups.addAll(reasoner.getEquivalentClasses(subClass).getEntities());
        for (OWLClass sup : sups) {
            binding=currentBinding.clone();
            binding[bindingPosition]=Clazz.create(sup.getIRI().toString());
            newBindings.add(binding);
        }
        return newBindings;
    }
    protected List<Atomic[]> checkSubsumption(OWLReasoner reasoner, OWLDataFactory datafactory, Atomic[] currentBinding, OWLClassExpression subClass, OWLClassExpression superClass) {
        // SubClassOf(:C :D)
        List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
        if (reasoner.isEntailed(datafactory.getOWLSubClassOfAxiom(subClass, superClass)))
            newBindings.add(currentBinding);
        return newBindings;
    }
    public <O> O accept(QueryObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
}
