package org.semanticweb.sparql.bgpevaluation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.HermiT.hierarchy.InstanceStatistics;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.sparql.arq.OWLOntologyGraph;
import org.semanticweb.sparql.bgpevaluation.queryobjects.QO_ClassAssertion;
import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.Variable;
import org.semanticweb.sparql.owlbgp.model.axioms.ClassAssertion;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.individuals.Individual;
import org.semanticweb.sparql.owlbgp.model.individuals.NamedIndividual;

public class BindingsIntersection {
	
	protected final OWLReasoner m_reasoner;
	protected final Reasoner m_hermit;
	protected final OWLDataFactory m_dataFactory;
    protected final OWLOntologyGraph m_graph;
    protected final InstanceStatistics m_instanceStatistics;
    Map<Variable,Integer> bindingPosition;
	
	public BindingsIntersection(OWLOntologyGraph graph, Map<Variable,Integer> bindingPositions ) {
		m_reasoner=graph.getReasoner();
	    m_dataFactory=graph.getOntology().getOWLOntologyManager().getOWLDataFactory();
	    m_graph=graph;
	    bindingPosition=bindingPositions;
		if (m_reasoner instanceof Reasoner) {
			m_hermit=(Reasoner)m_reasoner;
	    } else 
	    	throw new IllegalArgumentException("Error: The HermiT cost estimator can only be instantiated with a graph that has a (HermiT) Reasoner instance attached to it.");
		    m_instanceStatistics=m_hermit.getInstanceStatistics();
	}
	public List<Atomic[]> reduceClassBindings(List<Atomic[]> candidateBindings, QO_ClassAssertion queryObject) {
		if (candidateBindings.size()==0)
			return candidateBindings;	        
//	        List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
		 
	    ClassAssertion axiomTemplate=(ClassAssertion)queryObject.getAxiomTemplate();
//	        Set<Variable> vars=axiomTemplate.getVariablesInSignature();
//	        Set<Variable> indVars=axiomTemplate.getIndividual().getVariablesInSignature();
//	        Variable indVar=indVars.isEmpty()?null:indVars.iterator().next();
	        
	    ClassExpression ce=axiomTemplate.getClassExpression();
    	Set<Variable> ceVars=ce.getVariablesInSignature();
    	Individual ind=axiomTemplate.getIndividual();
    	List<Atomic[]> newBindings=new ArrayList<Atomic[]> ();
    	if (ceVars.isEmpty() && ind.isVariable()) {//C(?x)
    		int position=bindingPosition.get(ind);
            Set<OWLNamedIndividual> known=new HashSet<OWLNamedIndividual>();
            Set<OWLNamedIndividual> possible=new HashSet<OWLNamedIndividual>();
            m_instanceStatistics.getKnownAndPossibleInstances((OWLClass)ce.asOWLAPIObject(m_dataFactory),known,possible);
            Set<Atomic> knownAndPossibleSet=new HashSet<Atomic>();
            for (OWLNamedIndividual knownInd:known) {
            	knownAndPossibleSet.add(NamedIndividual.create(knownInd.getIRI().toString()));
            }
            for (OWLNamedIndividual possibleInd:possible) {
            	knownAndPossibleSet.add(NamedIndividual.create(possibleInd.getIRI().toString()));
            }
            Atomic[] binding;
            for (Atomic[] bind:candidateBindings) {
            	if (bind[position]==null){
            		for (Atomic at:knownAndPossibleSet){
            			binding=bind.clone();
                	    binding[position]=at;
                	    newBindings.add(binding);
                    }
                }  
                else if (knownAndPossibleSet.contains(bind[position]))
                	newBindings.add(bind);
            }
        }
    	return newBindings;
	}
/*	 
	 public List<Atomic[]> reduceObjectPropertyBindings(List<Atomic[]> candidateBindings, QO_ObjectPropertyAssertion queryObject) {
		  if (candidateBindings.size()==0)
	            return candidateBindings;
		 
	        ObjectPropertyAssertion axiomTemplate=(ObjectPropertyAssertion)queryObject.getAxiomTemplate();

	        Atomic ope=(Atomic)axiomTemplate.getObjectPropertyExpression();
	        Individual ind1=axiomTemplate.getIndividual1();
	        Individual ind2=axiomTemplate.getIndividual2();
	        
	        List<Atomic[]> bindingList=new ArrayList<Atomic[]>();
	        
	        if (!ope.isVariable() && ind1.isVariable() && ind2.isVariable()) {
	        	int[] positions=new int[2];
                positions[0]=bindingPosition.get(ind1);
                positions[1]=bindingPosition.get(ind2);
                Map<OWLNamedIndividual,Set<OWLNamedIndividual>>[] instances = m_hermit.getKnownAndPossiblePropertyInstances(AtomicRole.create(((Atomic)ope).getIdentifierString()));
                Map<Atomic,Set<Atomic>>[] individualMap=new HashMap[2];
                for (int i=0; i<instances.length;i++) {
                	Map<Atomic, Set<Atomic>> indHash=new HashMap<Atomic, Set<Atomic>>();
                	for (OWLNamedIndividual ind:instances[i].keySet()) {
                    	Set<Atomic> indSet=new HashSet<Atomic>();
                    	for (OWLNamedIndividual individual:instances[i].get(ind))        	
                    	  indSet.add(NamedIndividual.create(individual.getIRI().toString()));
                     	indHash.put(NamedIndividual.create((ind.getIRI()).toString()), indSet);
                    }
                	individualMap[i]=indHash;
                }
                for (Atomic[] bind:candidateBindings) {
//                  if (!candidateBindings.isEmpty()) {
                	Atomic[] binding;
                    if (bind[positions[0]]==null && bind[positions[1]]==null){
                    	for (int i=0; i<instances.length;i++) {
                    	  binding=bind.clone();
                    	  binding[positions[0]]=;
                    	  binding[positions[1]]=
                          newBindings.add(binding);
                    	}
                    }
                     if ((!individualMap[0].containsKey(bind[positions[0]])) && (!individualMap[1].containsKey(bind[positions[1]])))
                  	   candidateBindings.remove(bind);
                     else if (individualMap[0].containsKey(bind[positions[0]])) {
                    	if (!(individualMap[0].get(bind[positions[0]]).contains(bind[positions[1]])))
                    		candidateBindings.remove(bind);
                     }
                     else if (individualMap[1].containsKey(bind[positions[1]])) {
                    	 if (!(individualMap[1].get(bind[positions[0]]).contains(bind[positions[1]])))
                     		candidateBindings.remove(bind); 
                     }
                  }
  //              }  
	        }  
	        else if (!ope.isVariable() && !ind1.isVariable() && ind2.isVariable()) {
	        	int position;
                position=bindingPosition.get(ind2);
                Set<OWLNamedIndividual>[] instances = m_hermit.getKnownAndPossibleSuccessors(AtomicRole.create(((Atomic)ope).getIdentifierString()),
                		org.semanticweb.HermiT.model.Individual.create(((Atomic)ind1).getIdentifierString()));
                Set<Atomic>[] individualSet=new HashSet[2];
                for (int i=0; i<instances.length;i++) {
                    for (OWLNamedIndividual instance : instances[i]) {
                      individualSet[i].add(NamedIndividual.create(instance.getIRI().toString()));
                    }
                  }
                  for (Atomic[] bind:candidateBindings) {
                     if (!individualSet[0].contains(bind[position]) && !individualSet[1].contains(bind[position]))
                  	   candidateBindings.remove(bind);
                  }
            }   
	        else if (!ope.isVariable() && ind1.isVariable() && !ind2.isVariable()) {
	        	int position;
                position=bindingPosition.get(ind1); 
                Set<OWLNamedIndividual>[] instances = m_hermit.getKnownAndPossiblePredeccessors(AtomicRole.create(((Atomic)ope).getIdentifierString()),
                		org.semanticweb.HermiT.model.Individual.create(((Atomic)ind2).getIdentifierString()));
                Set<Atomic>[] individualSet=new HashSet[2];
                for (int i=0; i<instances.length;i++) {
                    for (OWLNamedIndividual instance : instances[i]) {
                      individualSet[i].add(NamedIndividual.create(instance.getIRI().toString()));
                    }
                  }
                  for (Atomic[] bind:candidateBindings) {
                     if (!individualSet[0].contains(bind[position]) && !individualSet[1].contains(bind[position]))
                  	   candidateBindings.remove(bind);
                  }
	        } 
	        return candidateBindings;
    }*/
}	 
