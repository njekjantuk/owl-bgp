package org.semanticweb.sparql.bgpevaluation.queryobjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.sparql.arq.HermiTGraph;
import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.BindingIterator;
import org.semanticweb.sparql.owlbgp.model.FromOWLAPIConverter;
import org.semanticweb.sparql.owlbgp.model.Variable;
import org.semanticweb.sparql.owlbgp.model.axioms.Axiom;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassVariable;
import org.semanticweb.sparql.owlbgp.model.dataranges.DatatypeVariable;
import org.semanticweb.sparql.owlbgp.model.individuals.IndividualVariable;
import org.semanticweb.sparql.owlbgp.model.properties.AnnotationPropertyVariable;
import org.semanticweb.sparql.owlbgp.model.properties.DataPropertyVariable;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyVariable;

public abstract class AbstractQueryObject<T extends Axiom> implements QueryObject<T> {
    
    public static int COST_ENTAILMENT=100;
    public static int COST_LOOKUP=1;
    public static int COST_CLASS_HIERARCHY_INSERTION=10*COST_ENTAILMENT;
    
    protected static final FromOWLAPIConverter s_fromOWLAPIConverter=new FromOWLAPIConverter();
    
    protected final T m_axiomTemplate;
    
    public AbstractQueryObject(T axiomTemplate) {
        m_axiomTemplate=axiomTemplate;
    }
    @Override
    public T getAxiomTemplate() {
        return m_axiomTemplate;
    }
    public List<Atomic[]> computeBindings(Reasoner reasoner, HermiTGraph graph, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions) {
        // if no solutions are computed yet, candidate bindings should have one all null array as an entry 
        // if candidateBindings is empty, there are no solutions already due to other constraints
        if (candidateBindings.size()==0)
            return candidateBindings;
        
        List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
        OWLDataFactory dataFactory=reasoner.getDataFactory();
        for (int i=0;i<candidateBindings.size();i++)
            newBindings.addAll(addBindings(reasoner, dataFactory, graph, candidateBindings.get(i), bindingPositions));
        return newBindings;
    }
    protected abstract List<Atomic[]> addBindings(Reasoner reasoner, OWLDataFactory dataFactory, HermiTGraph graph, Atomic[] currentBinding, Map<Variable,Integer> bindingPositions);

    protected List<Atomic[]> complex(Reasoner reasoner, OWLDataFactory dataFactory, HermiTGraph graph, Atomic[] currentBinding, Axiom axiom, Map<Variable,Integer> bindingPositions) {
        List<Atomic[]> results=new ArrayList<Atomic[]>();
        Atomic[] result;
        List<Variable> vars=new ArrayList<Variable>(axiom.getVariablesInSignature());
        Map<Variable,Set<? extends Atomic>> varToBindingSets=new HashMap<Variable, Set<? extends Atomic>>();
        for (Variable var : vars) {
            if (var instanceof ClassVariable)
                varToBindingSets.put(var, graph.getClassesInSignature());
            else if (var instanceof DatatypeVariable)
                varToBindingSets.put(var, graph.getDatatypesInSignature());
            else if (var instanceof ObjectPropertyVariable)
                varToBindingSets.put(var, graph.getObjectPropertiesInSignature());
            else if (var instanceof DataPropertyVariable)
                varToBindingSets.put(var, graph.getDataPropertiesInSignature());
            else if (var instanceof AnnotationPropertyVariable)
                varToBindingSets.put(var, graph.getAnnotationPropertiesInSignature());
            else if (var instanceof IndividualVariable)
                varToBindingSets.put(var, graph.getIndividualsInSignature());
            else 
                throw new IllegalArgumentException("Error: The class assertion axiom template "+axiom+" contains untyped variables. ");
        }
        for (Map<Variable,? extends Atomic> bindings : new BindingIterator(varToBindingSets)) {
            if (reasoner.isEntailed((OWLAxiom)axiom.getBoundVersion(bindings, dataFactory))) {
                result=currentBinding.clone();
                for (Variable var : bindings.keySet())
                    result[bindingPositions.get(var)]=bindings.get(var);
                results.add(result);
            }
        }
        return results;
    }
    protected int complexCost(List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions, HermiTGraph graph) {
        Set<Variable> vars=m_axiomTemplate.getVariablesInSignature();
        return complexCost(candidateBindings, bindingPositions, graph, vars);
    }
    protected int complexCost(List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions, HermiTGraph graph, Set<Variable> vars) {
        int cost=0;
        // complex
        boolean first=true;
        for (Variable var : vars) {
            int signatureSize=0;
            if (var instanceof ClassVariable)
                signatureSize=graph.getClassesInSignature().size();
            else if (var instanceof DatatypeVariable)
                signatureSize=graph.getDatatypesInSignature().size();
            else if (var instanceof ObjectPropertyVariable)
                signatureSize=graph.getObjectPropertiesInSignature().size();
            else if (var instanceof DataPropertyVariable)
                signatureSize=graph.getDataPropertiesInSignature().size();
            else if (var instanceof AnnotationPropertyVariable)
                signatureSize=graph.getAnnotationPropertiesInSignature().size();
            else if (var instanceof IndividualVariable)
                signatureSize=graph.getIndividualsInSignature().size();
            if (first)
                cost+=signatureSize;
            else 
                cost*=signatureSize;
        }
        return candidateBindings.size()*cost;
    }
    public String toString() {
        return m_axiomTemplate.toString();
    }
}
