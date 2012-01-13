package org.semanticweb.sparql.bgpevaluation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.sparql.arq.OWLOntologyGraph;
import org.semanticweb.sparql.bgpevaluation.queryobjects.QO_AsymmetricObjectProperty;
import org.semanticweb.sparql.bgpevaluation.queryobjects.QO_ClassAssertion;
import org.semanticweb.sparql.bgpevaluation.queryobjects.QO_DataPropertyAssertion;
import org.semanticweb.sparql.bgpevaluation.queryobjects.QO_FunctionalDataProperty;
import org.semanticweb.sparql.bgpevaluation.queryobjects.QO_FunctionalObjectProperty;
import org.semanticweb.sparql.bgpevaluation.queryobjects.QO_InverseFunctionalObjectProperty;
import org.semanticweb.sparql.bgpevaluation.queryobjects.QO_IrreflexiveObjectProperty;
import org.semanticweb.sparql.bgpevaluation.queryobjects.QO_NegativeDataPropertyAssertion;
import org.semanticweb.sparql.bgpevaluation.queryobjects.QO_NegativeObjectPropertyAssertion;
import org.semanticweb.sparql.bgpevaluation.queryobjects.QO_ObjectPropertyAssertion;
import org.semanticweb.sparql.bgpevaluation.queryobjects.QO_ReflexiveObjectProperty;
import org.semanticweb.sparql.bgpevaluation.queryobjects.QO_SubClassOf;
import org.semanticweb.sparql.bgpevaluation.queryobjects.QO_SubObjectPropertyOf;
import org.semanticweb.sparql.bgpevaluation.queryobjects.QO_SymmetricObjectProperty;
import org.semanticweb.sparql.bgpevaluation.queryobjects.QO_TransitiveObjectProperty;
import org.semanticweb.sparql.bgpevaluation.queryobjects.QueryObject;
import org.semanticweb.sparql.bgpevaluation.queryobjects.QueryObjectVisitorEx;
import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.Variable;
import org.semanticweb.sparql.owlbgp.model.axioms.Axiom;
import org.semanticweb.sparql.owlbgp.model.axioms.ClassAssertion;
import org.semanticweb.sparql.owlbgp.model.axioms.DataPropertyAssertion;
import org.semanticweb.sparql.owlbgp.model.axioms.NegativeDataPropertyAssertion;
import org.semanticweb.sparql.owlbgp.model.axioms.NegativeObjectPropertyAssertion;
import org.semanticweb.sparql.owlbgp.model.axioms.ObjectPropertyAssertion;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassVariable;
import org.semanticweb.sparql.owlbgp.model.dataranges.DatatypeVariable;
import org.semanticweb.sparql.owlbgp.model.individuals.Individual;
import org.semanticweb.sparql.owlbgp.model.individuals.IndividualVariable;
import org.semanticweb.sparql.owlbgp.model.literals.Literal;
import org.semanticweb.sparql.owlbgp.model.properties.AnnotationPropertyVariable;
import org.semanticweb.sparql.owlbgp.model.properties.DataProperty;
import org.semanticweb.sparql.owlbgp.model.properties.DataPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.properties.DataPropertyVariable;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectProperty;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyVariable;

public class StaticCostEstimationVisitor implements QueryObjectVisitorEx<double[]> {
    protected double COST_ENTAILMENT=100;
    protected double COST_LOOKUP=1;
    protected double COST_CLASS_HIERARCHY_INSERTION=10*COST_ENTAILMENT;
    
    protected final OWLReasoner m_reasoner;
    protected final OWLDataFactory m_dataFactory;
    protected final OWLOntologyGraph m_graph;
    protected final int m_classCount;
    protected final int m_opCount;
    protected final int m_dpCount;
    protected final int m_apCount;
    protected final int m_datatypeCount;
    protected final int m_indCount;
    protected final int m_litCount;
    
    protected Map<Variable,Integer> m_bindingPositions;
    protected List<Atomic[]> m_candidateBindings;
    
    public StaticCostEstimationVisitor(OWLOntologyGraph graph, Map<Variable,Integer> bindingPositions) {
        m_reasoner=graph.getReasoner();
        m_dataFactory=graph.getOntology().getOWLOntologyManager().getOWLDataFactory();
        m_graph=graph;
        m_classCount=graph.getClassesInSignature().size();
        m_opCount=graph.getObjectPropertiesInSignature().size();
        m_dpCount=graph.getDataPropertiesInSignature().size();
        m_apCount=graph.getAnnotationPropertiesInSignature().size();
        m_datatypeCount=graph.getDatatypesInSignature().size();
        m_indCount=graph.getIndividualsInSignature().size();
        m_litCount=graph.getLiteralsInSignature().size();
        m_bindingPositions=bindingPositions;
    }
   
    public double[] visit(QO_SubClassOf queryObject) {
        double[] result=new double[2];
        if (m_candidateBindings.isEmpty())
            return result; // no answers, no tests
        // check just one binding
        Atomic[] testBinding=m_candidateBindings.get(0);
        Axiom template=queryObject.getAxiomTemplate();
        Set<Variable> vars=queryObject.getAxiomTemplate().getVariablesInSignature();
        Map<Variable,Atomic> existingBindings=new HashMap<Variable,Atomic>();
        for (Variable var : vars) {
            Atomic binding=testBinding[m_bindingPositions.get(var)];
            if (binding!=null)
                existingBindings.put(var,binding);
        }
        Axiom instantiated=(Axiom)template.getBoundVersion(existingBindings);
        Set<Variable> unbound=instantiated.getVariablesInSignature();
        int results=1;
        for (int i=0;i<unbound.size();i++)
            results*=m_classCount;
        return new double[] { m_candidateBindings.size()*results*COST_LOOKUP, m_candidateBindings.size()*results };
    }
//    public double[] visit(QO_EquivalentClasses queryObject) {
//        return new double[] { 0, 0 };
//    }
//    public double[] visit(QO_DisjointClasses queryObject) {
//        return new double[] { 0, 0 };
//    }
//    public double[] visit(QO_DisjointUnion queryObject) {
//        return new double[] { 0, 0 };
//    }
    public double[] visit(QO_SubObjectPropertyOf queryObject) {
        double[] result=new double[2];
        if (m_candidateBindings.isEmpty())
            return result; // no answers, no tests
        // check just one binding
        Atomic[] testBinding=m_candidateBindings.get(0);
        Axiom template=queryObject.getAxiomTemplate();
        Set<Variable> vars=queryObject.getAxiomTemplate().getVariablesInSignature();
        Map<Variable,Atomic> existingBindings=new HashMap<Variable,Atomic>();
        for (Variable var : vars) {
            Atomic binding=testBinding[m_bindingPositions.get(var)];
            if (binding!=null)
                existingBindings.put(var,binding);
        }
        Axiom instantiated=(Axiom)template.getBoundVersion(existingBindings);
        Set<Variable> unbound=instantiated.getVariablesInSignature();
        int results=1;
        for (int i=0;i<unbound.size();i++)
            results*=m_opCount;
        return new double[] { m_candidateBindings.size()*results*COST_LOOKUP, m_candidateBindings.size()*results };
    }
//    public double[] visit(QO_EquivalentObjectProperties queryObject) {
//        return new double[] { 0, 0 };
//    }
//    public double[] visit(QO_DisjodointObjectProperties queryObject) {
//        return new double[] { 0, 0 };
//    }
//    public double[] visit(QO_InverseObjectProperties queryObject) {
//        return new double[] { 0, 0 };
//    }
//    public double[] visit(QO_ObjectPropertyDomain queryObject) {
//        return new double[] { 0, 0 };
//    }
//    public double[] visit(QO_ObjectPropertyRange queryObject) {
//        return new double[] { 0, 0 };
//    }
    public double[] visit(QO_FunctionalObjectProperty queryObject) {
        return getObjectPropertyAxiomCost(queryObject);
    }
    protected double[] getObjectPropertyAxiomCost(QueryObject<? extends Axiom> queryObject) {
        double[] result=new double[2];
        if (m_candidateBindings.isEmpty())
            return result; // no answers, no tests
        // check just one binding
        Atomic[] testBinding=m_candidateBindings.get(0);
        Set<Variable> opeVar=queryObject.getAxiomTemplate().getVariablesInSignature();
        if (opeVar.isEmpty() || testBinding[m_bindingPositions.get(opeVar.iterator().next())]!=null)
            return new double[] { COST_ENTAILMENT, 1 };
        else
            return new double[] { m_opCount*COST_ENTAILMENT, m_opCount }; // better return told numbers
    }
    protected double[] getDataPropertyAxiomCost(QueryObject<? extends Axiom> queryObject) {
        double[] result=new double[2];
        if (m_candidateBindings.isEmpty())
            return result; // no answers, no tests
        // check just one binding
        Atomic[] testBinding=m_candidateBindings.get(0);
        Set<Variable> dpeVar=queryObject.getAxiomTemplate().getVariablesInSignature();
        if (dpeVar.isEmpty() || testBinding[m_bindingPositions.get(dpeVar.iterator().next())]!=null)
            return new double[] { COST_ENTAILMENT, 1 };
        else
            return new double[] { m_dpCount*COST_ENTAILMENT, m_dpCount }; // better return told numbers
    }
    public double[] visit(QO_InverseFunctionalObjectProperty queryObject) {
        return getObjectPropertyAxiomCost(queryObject);
    }
    public double[] visit(QO_ReflexiveObjectProperty queryObject) {
        return getObjectPropertyAxiomCost(queryObject);
    }
    public double[] visit(QO_IrreflexiveObjectProperty queryObject) {
        return getObjectPropertyAxiomCost(queryObject);
    }
    public double[] visit(QO_SymmetricObjectProperty queryObject) {
        return getObjectPropertyAxiomCost(queryObject);
    }
    public double[] visit(QO_AsymmetricObjectProperty queryObject) {
        return getObjectPropertyAxiomCost(queryObject);
    }
    public double[] visit(QO_TransitiveObjectProperty queryObject) {
        return getObjectPropertyAxiomCost(queryObject);
    }
//    public double[] visit(QO_SubDataPropertyOf queryObject) {
//        return new double[] { 0, 0 };
//    }
//    public double[] visit(QO_EquivalentDataProperties queryObject) {
//        return new double[] { 0, 0 };
//    }
//    public double[] visit(QO_DisjointDataProperties queryObject) {
//        return new double[] { 0, 0 };
//    }
//    public double[] visit(QO_DataPropertyDomain queryObject) {
//        return new double[] { 0, 0 };
//    }
//    public double[] visit(QO_DataPropertyRange queryObject) {
//        return new double[] { 0, 0 };
//    }
    public double[] visit(QO_FunctionalDataProperty queryObject) {
        return getDataPropertyAxiomCost(queryObject);
    }
//    public double[] visit(QO_DatatypeDefinition queryObject) {
//        return new double[] { 0, 0 };
//    }
//    public double[] visit(QO_HasKey queryObject) {
//        return new double[] { 0, 0 };
//    }
//    public double[] visit(QO_SameIndividual queryObject) {
//        return new double[] { 0, 0 };
//    }
//    public double[] visit(QO_DifferentIndividuals queryObject) {
//        return new double[] { 0, 0 };
//    }
    public double[] visit(QO_ClassAssertion queryObject, Set<Variable> boundVar) {
        double[] estimate=new double[2];
        ClassAssertion axiomTemplate=queryObject.getAxiomTemplate();
        Set<Variable> vars=axiomTemplate.getVariablesInSignature();
        Set<Variable> indVars=axiomTemplate.getIndividual().getVariablesInSignature();
        Variable indVar=indVars.isEmpty()?null:indVars.iterator().next();
        Map<Variable,Atomic> existingBindings=new HashMap<Variable,Atomic>();
        Set<Variable> unbound=new HashSet<Variable>();
        
        double[] currentEstimate=getClassAssertionCost(axiomTemplate.getClassExpression(), axiomTemplate.getIndividual(), boundVar, indVar);
        estimate[0]+=currentEstimate[0];
        estimate[1]+=currentEstimate[1];
        
        return estimate;
    }
    protected double[] getClassAssertionCost(ClassExpression ce, Individual ind, Set<Variable> unbound, Variable indVar) {
        if (unbound.size()==0)
            return new double[] { COST_ENTAILMENT, 1 };
        else if (unbound.size()==1 && indVar!=null) // C(?x)
            return new double[] { m_indCount * COST_ENTAILMENT, m_indCount };
        else if (indVar==null && unbound.size()==1 && ce instanceof Atomic) // ?x(:a)
            return new double[] { m_classCount * COST_ENTAILMENT, m_classCount };
        else {
            double tests=complex(unbound);
            return new double[] { tests, tests }; // assume all tests succeed
        }
    }
    public double[] visit(QO_ObjectPropertyAssertion queryObject, Set<Variable> boundVar) {
        double[] estimate=new double[2];
        
        ObjectPropertyAssertion axiomTemplate=queryObject.getAxiomTemplate();
        Set<Variable> vars=axiomTemplate.getVariablesInSignature();
        Set<Variable> opVars=axiomTemplate.getObjectPropertyExpression().getVariablesInSignature();
        Variable opVar=opVars.isEmpty()?null:opVars.iterator().next();
        Map<Variable,Atomic> existingBindings=new HashMap<Variable,Atomic>();
            
        double[] currentEstimate=getObjectPropertyAssertionCost((ObjectProperty)axiomTemplate.getObjectPropertyExpression(), axiomTemplate.getIndividual1(), axiomTemplate.getIndividual2(), boundVar, opVar);
        estimate[0]+=currentEstimate[0];
        estimate[1]+=currentEstimate[1];
        
        return estimate;
    }
    protected double[] getObjectPropertyAssertionCost(ObjectProperty op, Individual ind1, Individual ind2, Set<Variable> unbound, Variable opVar) {
        if (unbound.size()==0)
            return new double[] { COST_ENTAILMENT, 1 };
        else if (unbound.size()==1 && opVar!=null) // ?x(i1, i2)
            return new double[] { m_opCount * COST_ENTAILMENT, m_opCount };
        else if (unbound.size()==1 && opVar==null) // op(:a ?x) or op(?x :a)  
            return new double[] { m_indCount * COST_ENTAILMENT, m_indCount };
        else if (unbound.size()==2 && opVar!=null) // ?x(:a ?y) or ?x(?y :a)  
            return new double[] { m_opCount * m_indCount * COST_ENTAILMENT, m_indCount * m_opCount };
        else if (unbound.size()==2 && opVar==null) // op(?x ?y)
            return new double[] { m_indCount * m_indCount * COST_ENTAILMENT, m_indCount * m_indCount };
        else {
            int tests=complex(unbound);
            return new double[] { tests, tests }; // assume all tests succeed
        }
    }
    public double[] visit(QO_NegativeObjectPropertyAssertion queryObject) {
        double[] estimate=new double[2];
        if (m_candidateBindings.isEmpty())
            return estimate; // no answers, no tests
        NegativeObjectPropertyAssertion axiomTemplate=queryObject.getAxiomTemplate();
        Set<Variable> vars=axiomTemplate.getVariablesInSignature();
        Set<Variable> opVars=axiomTemplate.getObjectPropertyExpression().getVariablesInSignature();
        Variable opVar=opVars.isEmpty()?null:opVars.iterator().next();
        Map<Variable,Atomic> existingBindings=new HashMap<Variable,Atomic>();
        Set<Variable> unbound=new HashSet<Variable>();
        for (Atomic[] testBinding : m_candidateBindings) {
            existingBindings.clear();
            for (Variable var : vars) {
                Atomic binding=testBinding[m_bindingPositions.get(var)];
                if (binding!=null)
                    existingBindings.put(var,binding);
            }
            NegativeObjectPropertyAssertion instantiated=(NegativeObjectPropertyAssertion)axiomTemplate.getBoundVersion(existingBindings);
            unbound.addAll(vars);
            unbound.removeAll(existingBindings.keySet());
            
            double[] currentEstimate=getObjectPropertyAssertionCost((ObjectProperty)instantiated.getObjectPropertyExpression(), instantiated.getIndividual1(), instantiated.getIndividual2(), unbound, opVar);
            estimate[0]+=currentEstimate[0];
            estimate[1]+=currentEstimate[1];
        }
        return estimate;
    }
    public double[] visit(QO_DataPropertyAssertion queryObject, Set<Variable> boundVar) {
        double[] estimate=new double[2];
       
        DataPropertyAssertion axiomTemplate=queryObject.getAxiomTemplate();
        Set<Variable> vars=axiomTemplate.getVariablesInSignature();
        Variable indVar=null;
        Variable dpVar=null;
        Variable litVar=null;
        for (Variable var : vars) {
            if (var instanceof DataPropertyVariable)
                dpVar=var;
            else if (var instanceof IndividualVariable)
                indVar=var;
            else 
                litVar=var;
        }
          
        double[] currentEstimate=getDataPropertyAssertionCost(axiomTemplate.getDataPropertyExpression(), axiomTemplate.getIndividual(), axiomTemplate.getLiteral(), boundVar, dpVar, indVar, litVar);
        estimate[0]+=currentEstimate[0];
        estimate[1]+=currentEstimate[1];
        
        return estimate;
    }
    protected double[] getDataPropertyAssertionCost(DataPropertyExpression dp, Individual ind, Literal lit, Set<Variable> unbound, Variable dpVar, Variable indVar, Variable litVar) {
        if (unbound.size()==0)
            return new double[] { COST_ENTAILMENT, 1 };
        else if (unbound.size()==1 && dpVar!=null) // ?x(i, lit)
            return new double[] { m_dpCount * COST_ENTAILMENT, m_dpCount };
        else if (unbound.size()==1 && indVar!=null) // dp(?x :a)  
            return new double[] { m_indCount * COST_ENTAILMENT, m_indCount };
        else if (unbound.size()==1 && litVar!=null) // dp(:a ?x)  
            return new double[] { m_litCount * COST_ENTAILMENT, m_litCount };
        else if (unbound.size()==2 && litVar==null) // ?x(?y :a)  
            return new double[] { m_dpCount * m_indCount * COST_ENTAILMENT, m_indCount * m_dpCount };
        else if (unbound.size()==2 && indVar==null) // ?x(:a ?y)  
            return new double[] { m_dpCount * m_litCount * COST_ENTAILMENT, m_litCount * m_dpCount };
        else if (unbound.size()==2 && dpVar==null) // dp(?x ?y)
            return new double[] { m_indCount * m_litCount * COST_ENTAILMENT, m_indCount * m_litCount };
        else {
            int tests=complex(unbound);
            return new double[] { tests, tests }; // assume all tests succeed
        }
    }
    public double[] visit(QO_NegativeDataPropertyAssertion queryObject) {
        double[] estimate=new double[2];
        if (m_candidateBindings.isEmpty())
            return estimate; // no answers, no tests
        NegativeDataPropertyAssertion axiomTemplate=queryObject.getAxiomTemplate();
        Set<Variable> vars=axiomTemplate.getVariablesInSignature();
        Variable indVar=null;
        Variable dpVar=null;
        Variable litVar=null;
        for (Variable var : vars) {
            if (var instanceof DataPropertyVariable)
                dpVar=var;
            else if (var instanceof IndividualVariable)
                indVar=var;
            else 
                litVar=var;
        }
        Map<Variable,Atomic> existingBindings=new HashMap<Variable,Atomic>();
        Set<Variable> unbound=new HashSet<Variable>();
        for (Atomic[] testBinding : m_candidateBindings) {
            existingBindings.clear();
            for (Variable var : vars) {
                Atomic binding=testBinding[m_bindingPositions.get(var)];
                if (binding!=null)
                    existingBindings.put(var,binding);
            }
            NegativeDataPropertyAssertion instantiated=(NegativeDataPropertyAssertion)axiomTemplate.getBoundVersion(existingBindings);
            unbound.addAll(vars);
            unbound.removeAll(existingBindings.keySet());
            
            double[] currentEstimate=getDataPropertyAssertionCost((DataProperty)instantiated.getDataPropertyExpression(), instantiated.getIndividual(), instantiated.getLiteral(), unbound, dpVar, indVar, litVar);
            estimate[0]+=currentEstimate[0];
            estimate[1]+=currentEstimate[1];
        }
        return estimate;
    }

    protected int complex(Set<Variable> unbound) {
        int tests=0;
        // complex
        boolean first=true;
        for (Variable var : unbound) {
            int signatureSize=0;
            if (var instanceof ClassVariable)
                signatureSize=m_classCount;
            else if (var instanceof DatatypeVariable)
                signatureSize=m_datatypeCount;
            else if (var instanceof ObjectPropertyVariable)
                signatureSize=m_opCount;
            else if (var instanceof DataPropertyVariable)
                signatureSize=m_dpCount;
            else if (var instanceof AnnotationPropertyVariable)
                signatureSize=m_apCount;
            else if (var instanceof IndividualVariable)
                signatureSize=m_indCount;
            if (first)
                tests+=signatureSize;
            else 
                tests*=signatureSize;
        }
        return tests;
    }

	@Override
	public double[] visit(QO_ClassAssertion axiom) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double[] visit(QO_ObjectPropertyAssertion axiom) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double[] visit(QO_DataPropertyAssertion axiom) {
		// TODO Auto-generated method stub
		return null;
	}
}

