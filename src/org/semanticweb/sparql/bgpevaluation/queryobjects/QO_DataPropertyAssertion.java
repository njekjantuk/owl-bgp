package org.semanticweb.sparql.bgpevaluation.queryobjects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.sparql.arq.OWLOntologyGraph;
import org.semanticweb.sparql.bgpevaluation.QueryObjectVisitorEx;
import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.Variable;
import org.semanticweb.sparql.owlbgp.model.axioms.DataPropertyAssertion;
import org.semanticweb.sparql.owlbgp.model.dataranges.Datatype;
import org.semanticweb.sparql.owlbgp.model.individuals.Individual;
import org.semanticweb.sparql.owlbgp.model.individuals.NamedIndividual;
import org.semanticweb.sparql.owlbgp.model.literals.Literal;
import org.semanticweb.sparql.owlbgp.model.literals.TypedLiteral;
import org.semanticweb.sparql.owlbgp.model.properties.DataProperty;

public class QO_DataPropertyAssertion  extends AbstractQueryObject<DataPropertyAssertion> {

    public QO_DataPropertyAssertion(DataPropertyAssertion axiomTemplate) {
        super(axiomTemplate);
    }
    protected List<Atomic[]> addBindings(OWLReasoner reasoner, OWLDataFactory dataFactory, OWLOntologyGraph graph, Atomic[] currentBinding, Map<Variable,Integer> bindingPositions) {
        Map<Variable,Atomic> bindingMap=new HashMap<Variable, Atomic>();
        // apply bindings that are already computed from previous steps
        for (Variable var : bindingPositions.keySet())
            bindingMap.put(var, currentBinding[bindingPositions.get(var)]);
        try {
            DataPropertyAssertion assertion=((DataPropertyAssertion)m_axiomTemplate.getBoundVersion(bindingMap));
            Atomic dpe=(Atomic)assertion.getDataPropertyExpression();
            Individual ind=assertion.getIndividual();
            Literal lit=assertion.getLiteral();
            if (dpe.isVariable() && ind.isVariable() && lit.isVariable()) { 
                // ?x(?y ?z)
                int[] positions=new int[3];
                positions[0]=bindingPositions.get(dpe);
                positions[1]=bindingPositions.get(ind);
                positions[2]=bindingPositions.get(lit);
                return compute012UnBound(reasoner,currentBinding,positions);
            } else if (dpe.isVariable() && ind.isVariable() && !lit.isVariable()) {
                // ?x(?y lit)
                int[] positions=new int[2];
                positions[0]=bindingPositions.get(dpe);
                positions[1]=bindingPositions.get(ind);
                return compute2Bound(reasoner,currentBinding,(OWLLiteral)lit.asOWLAPIObject(dataFactory),positions);
            } else if (dpe.isVariable() && !ind.isVariable() && lit.isVariable()) {
                // ?x(:a ?z)
                int[] positions=new int[2];
                positions[0]=bindingPositions.get(dpe);
                positions[1]=bindingPositions.get(lit);
                return compute1Bound(reasoner,currentBinding,(OWLNamedIndividual)ind.asOWLAPIObject(dataFactory),positions);
            } else if (dpe.isVariable() && !ind.isVariable() && !lit.isVariable()) {
                // ?x(:a lit)
                int[] positions=new int[1];
                positions[0]=bindingPositions.get(dpe);
                return compute12Bound(reasoner,currentBinding,(OWLNamedIndividual)ind.asOWLAPIObject(dataFactory),(OWLLiteral)lit.asOWLAPIObject(dataFactory),positions);
            } else if (!dpe.isVariable() && ind.isVariable() && lit.isVariable()) {
                // dp(?y ?z)
                int[] positions=new int[2];
                positions[0]=bindingPositions.get(ind);
                positions[1]=bindingPositions.get(lit);
                return compute0Bound(reasoner,currentBinding,(OWLDataProperty)dpe.asOWLAPIObject(dataFactory),positions);
            } else if (!dpe.isVariable() && ind.isVariable() && !lit.isVariable()) {
                // dp(?y lit)
                int[] positions=new int[1];
                positions[0]=bindingPositions.get(ind);
                return compute02Bound(reasoner,currentBinding,(OWLDataProperty)dpe.asOWLAPIObject(dataFactory),(OWLLiteral)lit.asOWLAPIObject(dataFactory),positions);
            } else if (!dpe.isVariable() && !ind.isVariable() && lit.isVariable()) {
                // dp(:a ?z)
                int[] positions=new int[1];
                positions[0]=bindingPositions.get(lit);
                return compute01Bound(reasoner,currentBinding,(OWLDataProperty)dpe.asOWLAPIObject(dataFactory),(OWLNamedIndividual)ind.asOWLAPIObject(dataFactory),positions);
            } else if (!dpe.isVariable() && !ind.isVariable() && !lit.isVariable()) {
                // dp(:a lit)
                if (compute012Bound(reasoner,currentBinding,(OWLDataProperty)dpe.asOWLAPIObject(dataFactory),(OWLNamedIndividual)ind.asOWLAPIObject(dataFactory),(OWLLiteral)lit.asOWLAPIObject(dataFactory)))
                    return Collections.singletonList(currentBinding);
                else 
                    return new ArrayList<Atomic[]>();
            } else {
                return complex(reasoner,dataFactory,graph,currentBinding,assertion,bindingPositions);
            }
        } catch (IllegalArgumentException e) {
            // current binding is incompatible will not add new bindings in newBindings
            return new ArrayList<Atomic[]>();
        }
    }
    protected List<Atomic[]> compute012UnBound(OWLReasoner reasoner,Atomic[] currentBinding, int[] bindingPositions) {
        // DataPropertyAssertion(?x ?y ?z)
        List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
        Atomic[] binding;
        OWLOntology ont=reasoner.getRootOntology();
        for (OWLDataProperty owlDp : ont.getDataPropertiesInSignature(true)) {
            DataProperty dp=DataProperty.create(owlDp.getIRI().toString());
            for (OWLNamedIndividual owlInd : ont.getIndividualsInSignature(true)) {
                NamedIndividual ind=NamedIndividual.create(owlInd.getIRI().toString());
                for (OWLLiteral lit : reasoner.getDataPropertyValues(owlInd, owlDp)) {
                    binding=currentBinding.clone();
                    binding[bindingPositions[0]]=dp;
                    binding[bindingPositions[1]]=ind;
                    binding[bindingPositions[2]]=TypedLiteral.create(lit.getLiteral(), lit.getLang(), Datatype.create(lit.getDatatype().getIRI().toString()));
                    newBindings.add(binding);
                }
            }
        }
        return newBindings;
    }
    protected List<Atomic[]> compute2Bound(OWLReasoner reasoner, Atomic[] currentBinding, OWLLiteral lit, int[] bindingPositions) {
        // DataPropertyAssertion(?x ?y :a)
        List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
        Atomic[] binding;
        OWLOntology ont=reasoner.getRootOntology();
        for (OWLDataProperty owlDp : ont.getDataPropertiesInSignature(true)) {
            DataProperty dp=DataProperty.create(owlDp.getIRI().toString());
            for (OWLNamedIndividual owlInd : ont.getIndividualsInSignature(true)) {
                if (reasoner.getDataPropertyValues(owlInd, owlDp).contains(lit)) {
                    binding=currentBinding.clone();
                    binding[bindingPositions[0]]=dp;
                    binding[bindingPositions[1]]=NamedIndividual.create(owlInd.getIRI().toString());
                    newBindings.add(binding);
                }
            }
        }
        return newBindings;
    }
    protected List<Atomic[]> compute1Bound(OWLReasoner reasoner, Atomic[] currentBinding, OWLNamedIndividual ind, int[] bindingPositions) {
        // DataPropertyAssertion(?x :a ?y)
        List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
        Atomic[] binding;
        OWLOntology ont=reasoner.getRootOntology();
        for (OWLDataProperty owlDp : ont.getDataPropertiesInSignature(true)) {
            DataProperty dp=DataProperty.create(owlDp.getIRI().toString());
            for (OWLLiteral lit : reasoner.getDataPropertyValues(ind, owlDp)) {
                binding=currentBinding.clone();
                binding[bindingPositions[0]]=dp;
                binding[bindingPositions[1]]=TypedLiteral.create(lit.getLiteral(), lit.getLang(), Datatype.create(lit.getDatatype().getIRI().toString()));
                newBindings.add(binding);
            }
        }
        return newBindings;
    }
    protected List<Atomic[]> compute12Bound(OWLReasoner reasoner, Atomic[] currentBinding, OWLNamedIndividual ind, OWLLiteral lit, int[] bindingPositions) {
        // DataPropertyAssertion(?x :a :b)
        List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
        Atomic[] binding;
        for (OWLDataProperty owlDp : reasoner.getRootOntology().getDataPropertiesInSignature(true)) {
            if (reasoner.getDataPropertyValues(ind, owlDp).contains(lit)) {
                binding=currentBinding.clone();
                binding[bindingPositions[0]]=DataProperty.create(owlDp.getIRI().toString());
            }
        }
        return newBindings;
    }
    protected List<Atomic[]> compute0Bound(OWLReasoner reasoner, Atomic[] currentBinding, OWLDataProperty dpe, int[] bindingPositions) {
        // DataPropertyAssertion(:r ?x ?y)
        List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
        Atomic[] binding;
        for (OWLNamedIndividual owlInd : reasoner.getRootOntology().getIndividualsInSignature(true)) {
            NamedIndividual ind=NamedIndividual.create(owlInd.getIRI().toString());
            for (OWLLiteral lit : reasoner.getDataPropertyValues(owlInd, dpe)) {
                binding=currentBinding.clone();
                binding[bindingPositions[1]]=ind;
                binding[bindingPositions[0]]=TypedLiteral.create(lit.getLiteral(), lit.getLang(), Datatype.create(lit.getDatatype().getIRI().toString()));
                newBindings.add(binding);
            }
        }
        return newBindings;
    }
    protected List<Atomic[]> compute02Bound(OWLReasoner reasoner, Atomic[] currentBinding, OWLDataProperty dpe, OWLLiteral lit, int[] bindingPositions) {
        // DataPropertyAssertion(:r ?x :a)
        List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
        Atomic[] binding;
        for (OWLNamedIndividual owlInd : reasoner.getRootOntology().getIndividualsInSignature(true)) {
            if (reasoner.getDataPropertyValues(owlInd, dpe).contains(lit)) {
                binding=currentBinding.clone();
                binding[bindingPositions[0]]=NamedIndividual.create(owlInd.getIRI().toString());
            }
        }
        return newBindings;
    }
    protected List<Atomic[]> compute01Bound(OWLReasoner reasoner, Atomic[] currentBinding, OWLDataProperty dpe, OWLNamedIndividual ind, int[] bindingPositions) {
        // DataPropertyAssertion(:r :a ?y)
        Atomic[] binding;
        List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
        for (OWLLiteral lit : reasoner.getDataPropertyValues(ind, dpe)) {
            binding=currentBinding.clone();
            binding[bindingPositions[0]]=TypedLiteral.create(lit.getLiteral(), lit.getLang(), Datatype.create(lit.getDatatype().getIRI().toString()));
            newBindings.add(binding);
        }
        return newBindings;
    }
    protected boolean compute012Bound(OWLReasoner reasoner, Atomic[] currentBinding, OWLDataProperty dpe, OWLNamedIndividual ind, OWLLiteral lit) {
        // DataPropertyAssertion(:r :a :b)
        return (reasoner.getDataPropertyValues(ind, dpe).contains(lit));
    }
    public <O> O accept(QueryObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
}