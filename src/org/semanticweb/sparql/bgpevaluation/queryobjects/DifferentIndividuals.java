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
