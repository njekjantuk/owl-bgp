package org.semanticweb.sparql.evaluation.queryobjects;


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
