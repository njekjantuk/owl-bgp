package org.semanticweb.sparql.evaluation.queryobjects;


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
