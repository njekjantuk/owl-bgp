/* Copyright 2010-2012 by the developers of the OWL-BGP project. 

   This file is part of the OWL-BGP project.

   OWL-BGP is free software: you can redistribute it and/or modify
   it under the terms of the GNU Lesser General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   OWL-BGP is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU Lesser General Public License for more details.

   You should have received a copy of the GNU Lesser General Public License
   along with OWL-BGP.  If not, see <http://www.gnu.org/licenses/>.
*/


package  org.semanticweb.sparql.bgpevaluation.queryobjects;


public class ObjectPropertyRange {
//    protected final OWLObjectPropertyExpression op;
//    protected final OWLClass c;
//	
//	public ObjectPropertyRange(List<OWLObject> initialBinding, int[] bindingPositions) {
//	    super(initialBinding, bindingPositions);
//	    assert initialBinding.get(bindingPositions[0]) instanceof OWLClass;
//        assert initialBinding.get(bindingPositions[1]) instanceof OWLObjectPropertyExpression;
//        this.op=(OWLObjectPropertyExpression)initialBinding.get(bindingPositions[0]);
//		this.c=(OWLClass)initialBinding.get(bindingPositions[1]);
//	}
//	protected List<OWLObject[]> addBindings(Reasoner reasoner, OWLObject[] candidateBinding) {
//	    OWLObject[] binding;
//		List<OWLObject[]> newBindings=new ArrayList<OWLObject[]>();
//		
//		OWLObject candidateOP=candidateBinding[bindingPositions[0]];
//		OWLObject candidateClass=candidateBinding[bindingPositions[1]];
//        OWLObjectPropertyExpression ope;
//        OWLClass cls;
//        if ((candidateOP!=null && !(candidateOP instanceof OWLObjectPropertyExpression)) 
//                || (candidateClass !=null && !(candidateClass instanceof OWLClass))) {
//            // candidate is incompatible will not add new bindings in newBindings
//            return newBindings;
//        } else {
//            ope=(OWLObjectPropertyExpression)candidateBinding[bindingPositions[1]];
//            cls=(OWLClass)candidateBinding[bindingPositions[1]];
//        }
//		if (candidateOP==null) {
//			if (candidateClass==null) {
//				// ObjectPropertyRange(?x ?y)
//			    for (OWLObjectProperty op : reasoner.getRootOntology().getObjectPropertiesInSignature(true)) {
//					Set<OWLClass> ranges=reasoner.getObjectPropertyDomains(op, false).getFlattened();
//					for (OWLClass range : ranges) {
//						binding=candidateBinding.clone();
//						binding[bindingPositions[0]]=op;
//						binding[bindingPositions[1]]=range;
//						newBindings.add(binding);
//					}
//				}
//			} else {
//				// ObjectPropertyRange(?x C)
//			    for (OWLObjectProperty op : reasoner.getRootOntology().getObjectPropertiesInSignature(true)) {
//    				Set<OWLClass> ranges=reasoner.getObjectPropertyDomains(op,false).getFlattened();
//    				if (ranges.contains(cls)) {
//    					binding=candidateBinding.clone();
//    					binding[bindingPositions[0]]=op;
//    					newBindings.add(binding);
//    				}
//			    }
//			}
//		} else {
//			if (cls==null) {
//				// ObjectPropertyRange(op ?x) 
//				Set<OWLClass> ranges=reasoner.getObjectPropertyDomains(op, false).getFlattened();
//				for (OWLClass range : ranges) {
//					binding=candidateBinding.clone();
//					binding[bindingPositions[1]]=range;
//					newBindings.add(binding);
//				}
//			} else {
//				// ObjectPropertyRange(:op :C)
//				if (reasoner.isEntailed(reasoner.getDataFactory().getOWLObjectPropertyRangeAxiom(ope, cls)))
//					newBindings.add(candidateBinding);
//			}
//		} 
//		return newBindings;
//	}
}
