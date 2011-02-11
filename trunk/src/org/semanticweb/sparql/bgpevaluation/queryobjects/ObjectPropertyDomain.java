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


public class ObjectPropertyDomain {
//    protected final OWLObjectPropertyExpression op;
//    protected final OWLClass c;
//	
//	public ObjectPropertyDomain(List<OWLObject> initialBinding, int[] bindingPositions) {
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
//				// ObjectPropertyDomain(?x ?y)
//			    for (OWLObjectProperty op : reasoner.getRootOntology().getObjectPropertiesInSignature(true)) {
//					NodeSet<OWLClass> domains=reasoner.getObjectPropertyDomains(op, false);
//					for (OWLClass owlClass : domains.getFlattened()) {
//						binding=candidateBinding.clone();
//						binding[bindingPositions[0]]=op;
//						binding[bindingPositions[1]]=owlClass;
//						newBindings.add(binding);
//					}
//				}
//			} else {
//				// ObjectPropertyDomain(?x C)
//			    for (OWLObjectProperty op : reasoner.getRootOntology().getObjectPropertiesInSignature(true)) {
//    				Set<OWLClass> domains=reasoner.getObjectPropertyDomains(op,false).getFlattened();
//    				if (domains.contains(cls)) {
//    					binding=candidateBinding.clone();
//    					binding[bindingPositions[0]]=op;
//    					newBindings.add(binding);
//    				}
//			    }
//			}
//		} else {
//			if (cls==null) {
//				// ObjectPropertyDomain(op ?x) 
//				Set<OWLClass> domains=reasoner.getObjectPropertyDomains(op, false).getFlattened();
//				for (OWLClass domain : domains) {
//					binding=candidateBinding.clone();
//					binding[bindingPositions[1]]=domain;
//					newBindings.add(binding);
//				}
//			} else {
//				// ObjectPropertyDomain(:op :C)
//				if (reasoner.isEntailed(reasoner.getDataFactory().getOWLObjectPropertyDomainAxiom(ope, cls)))
//					newBindings.add(candidateBinding);
//			}
//		} 
//		return newBindings;
//	}
}
