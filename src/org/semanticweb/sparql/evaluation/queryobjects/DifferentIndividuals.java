package org.semanticweb.sparql.evaluation.queryobjects;


public class DifferentIndividuals {
//
//	protected final OWLNamedIndividual ind1;
//	protected final OWLNamedIndividual ind2;
//	
//	public DifferentIndividuals(List<OWLObject> initialBinding, int[] bindingPositions) {
//	    super(initialBinding, bindingPositions);
//		assert initialBinding.get(bindingPositions[0]) instanceof OWLNamedIndividual;
//		assert initialBinding.get(bindingPositions[1]) instanceof OWLNamedIndividual;
//		this.ind1=(OWLNamedIndividual)initialBinding.get(bindingPositions[0]);
//		this.ind2=(OWLNamedIndividual)initialBinding.get(bindingPositions[1]);
//	}
//	
//	protected List<OWLObject[]> addBindings(Reasoner reasoner, OWLObject[] candidateBinding) {
//		OWLObject[] binding;
//		List<OWLObject[]> newBindings=new ArrayList<OWLObject[]>();
//		OWLObject candidateIndividual1=candidateBinding[bindingPositions[0]];
//		OWLObject candidateIndividual2=candidateBinding[bindingPositions[1]];
//		OWLNamedIndividual individual1;
//		OWLNamedIndividual individual2;
//		if ((candidateIndividual1!=null && !(candidateIndividual1 instanceof OWLNamedIndividual)) 
//				|| (candidateIndividual2 !=null && !(candidateIndividual2 instanceof OWLNamedIndividual))) {
//			// candidate is incompatible will not add new bindings in newBindings
//			return newBindings;
//		} else {
//			individual1=(OWLNamedIndividual)candidateBinding[bindingPositions[0]];
//			individual2=(OWLNamedIndividual)candidateBinding[bindingPositions[1]];
//			if (candidateIndividual1==null && candidateIndividual2==null) {
//				// DifferentIndividuals(?x ?y)
//				for (Individual i : reasoner.getDLOntology().getAllIndividuals()) {
//					if (!Prefixes.isInternalIRI(i.getIRI())) {
//						OWLNamedIndividual owlIndividual=reasoner.getDataFactory().getOWLNamedIndividual(IRI.create(i.getIRI()));
//						NodeSet<OWLNamedIndividual> instances=reasoner.getDifferentIndividuals(owlIndividual);
//						for (OWLNamedIndividual ind : instances.getFlattened()) {
//							binding=candidateBinding.clone();
//							binding[bindingPositions[0]]=owlIndividual;
//							binding[bindingPositions[1]]=ind;
//							newBindings.add(binding);
//						}
//					}
//				}
//			} else if (candidateIndividual1!=null && candidateIndividual2!=null) {
//				// DifferentIndividuals(:a :b)
//				if (reasoner.isEntailed(reasoner.getDataFactory().getOWLDifferentIndividualsAxiom((OWLIndividual)candidateIndividual1,(OWLIndividual)candidateIndividual2)))
//					newBindings.add(candidateBinding);
//			} else {
//				OWLNamedIndividual knownInd;
//				int bindingIndex;
//				if (candidateIndividual1!=null) {
//					// DifferentIndividuals(:a ?x)
//					knownInd=individual1;
//					bindingIndex=0;
//				} else {
//					// DifferentIndividuals(?x :a)
//					knownInd=individual2;
//					bindingIndex=1;
//				}
//				Set<OWLNamedIndividual> differents=reasoner.getDifferentIndividuals(knownInd).getFlattened();
//				for (OWLNamedIndividual different : differents) {
//					binding=candidateBinding.clone();
//					binding[bindingPositions[bindingIndex]]=different;
//					newBindings.add(binding);
//				}
//			} 
//		}
//		return newBindings;
//	}
}
