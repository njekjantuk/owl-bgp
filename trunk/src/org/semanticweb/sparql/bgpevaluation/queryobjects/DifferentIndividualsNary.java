package org.semanticweb.sparql.bgpevaluation.queryobjects;


public class DifferentIndividualsNary {
//
//	protected final OWLNamedIndividual[] inds;
//	protected final Axiom axiom;
//	
//	public DifferentIndividualsNary(List<OWLObject> initialBinding, int[] bindingPositions, Axiom axiom) {
//	    super(initialBinding, bindingPositions);
//	    inds=new OWLNamedIndividual[bindingPositions.length];
//	    for (int i : bindingPositions) { 
//		  assert initialBinding.get(bindingPositions[i]) instanceof OWLNamedIndividual;
//		  this.inds[i]=(OWLNamedIndividual)initialBinding.get(bindingPositions[i]);
//	    }
//	    this.axiom=axiom;
//	}
//	
//	protected List<OWLObject[]> addBindings(Reasoner reasoner, OWLObject[] candidateBinding) {
//		OWLObject[] binding;
//		List<OWLObject[]> newBindings=new ArrayList<OWLObject[]>();
//		Map<Variable,Set<Atomic>> varToBindingSets=new HashMap<Variable, Set<Atomic>>();
//		for (int i : bindingPositions) {
//		    if (candidateBinding[i]!=null) {
//		        if (!(candidateBinding[i] instanceof OWLNamedIndividual))
//		          return newBindings;
//		        Set<Atomic> bindings=new HashSet<Atomic>();
//		        bindings.add(NamedIndividual.create(candidateBinding[i].toString()));
////		        varToBindingSets.put(  , bindings);
//		    }
//		}
//        for (Map<Variable,Atomic> bindings : new BindingIterator(varToBindingSets)) {
//            ExtendedOWLObject bound=axiom.getBoundVersion(bindings);
//            
//        }
//		return newBindings;
//	}
}
