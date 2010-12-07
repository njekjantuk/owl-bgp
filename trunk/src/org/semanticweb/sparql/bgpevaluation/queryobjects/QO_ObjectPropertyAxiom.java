package org.semanticweb.sparql.bgpevaluation.queryobjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.sparql.arq.OWLOntologyGraph;
import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.Variable;
import org.semanticweb.sparql.owlbgp.model.axioms.ObjectPropertyAxiom;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectInverseOf;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectProperty;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;

public abstract class QO_ObjectPropertyAxiom<T extends ObjectPropertyAxiom> extends AbstractQueryObject<T> {

    public QO_ObjectPropertyAxiom(T axiomTemplate) {
	    super(axiomTemplate);
	}
	protected List<Atomic[]> addBindings(OWLReasoner reasoner, OWLDataFactory dataFactory, OWLOntologyGraph graph, Atomic[] currentBinding, Map<Variable,Integer> bindingPositions) {
		Map<Variable,Atomic> bindingMap=new HashMap<Variable, Atomic>();
		// apply bindings that are already computed from previous steps
		for (Variable var : bindingPositions.keySet())
		    bindingMap.put(var, currentBinding[bindingPositions.get(var)]);
		try {
		    @SuppressWarnings("unchecked")
            T assertion=(T)m_axiomTemplate.getBoundVersion(bindingMap);
    		ObjectPropertyExpression ope=assertion.getObjectPropertyExpression().getNormalized();
            if (ope.isVariable()) {
                int position=bindingPositions.get(ope);
                return compute(reasoner,dataFactory,currentBinding,position,false);
            } else if (ope instanceof ObjectInverseOf) {
                ObjectInverseOf inv=(ObjectInverseOf)ope;
                if (inv.getInvertedObjectProperty().isVariable()) { 
                    int position=bindingPositions.get(ope);
                    return compute(reasoner,dataFactory,currentBinding,position,true);
                }
            } 
            return check(reasoner,dataFactory,currentBinding,(OWLObjectPropertyExpression)ope.asOWLAPIObject(dataFactory));
		} catch (IllegalArgumentException e) {
		    // current binding is incompatible will not add new bindings in newBindings
		    return new ArrayList<Atomic[]>();
		}
	}
	
	protected abstract OWLAxiom getEntailmentAxiom(OWLDataFactory dataFactory, OWLObjectPropertyExpression ope);
	
    protected List<Atomic[]> check(OWLReasoner reasoner, OWLDataFactory dataFactory, Atomic[] currentBinding, OWLObjectPropertyExpression ope) {
	    List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
        if (reasoner.isEntailed(getEntailmentAxiom(dataFactory, ope))) 
            newBindings.add(currentBinding);
        return newBindings;
    }
	protected List<Atomic[]> compute(OWLReasoner reasoner, OWLDataFactory dataFactory, Atomic[] currentBinding, int bindingPosition, boolean inverse) {
	    // FunctionalObjectProperty(?x)
        Atomic[] binding;
        List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
        for (OWLObjectProperty op : reasoner.getRootOntology().getObjectPropertiesInSignature(true)) {
            OWLObjectPropertyExpression ope=inverse ? dataFactory.getOWLObjectInverseOf(op) : op;
            if (reasoner.isEntailed(getEntailmentAxiom(dataFactory, ope))) {
                binding=currentBinding.clone();
                binding[bindingPosition]=ObjectProperty.create(op.getIRI().toString());
                newBindings.add(binding);
            }
        }
        return newBindings;
    }
}
