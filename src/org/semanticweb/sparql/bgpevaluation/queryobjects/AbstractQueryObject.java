package org.semanticweb.sparql.bgpevaluation.queryobjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.sparql.arq.OWLOntologyGraph;
import org.semanticweb.sparql.bgpevaluation.QueryObjectVisitorEx;
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
    
    protected static final FromOWLAPIConverter s_fromOWLAPIConverter=new FromOWLAPIConverter();
    
    protected final T m_axiomTemplate;
    
    public AbstractQueryObject(T axiomTemplate) {
        m_axiomTemplate=axiomTemplate;
    }
    @Override
    public T getAxiomTemplate() {
        return m_axiomTemplate;
    }
    public List<Atomic[]> computeBindings(OWLReasoner reasoner, OWLOntologyGraph graph, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions) {
        // if no solutions are computed yet, candidate bindings should have one all null array as an entry 
        // if candidateBindings is empty, there are no solutions already due to other constraints
        if (candidateBindings.size()==0)
            return candidateBindings;
        
        List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
        OWLDataFactory dataFactory=reasoner.getRootOntology().getOWLOntologyManager().getOWLDataFactory();
        for (int i=0;i<candidateBindings.size();i++)
            newBindings.addAll(addBindings(reasoner, dataFactory, graph, candidateBindings.get(i), bindingPositions));
        return newBindings;
    }
    protected abstract List<Atomic[]> addBindings(OWLReasoner reasoner, OWLDataFactory dataFactory, OWLOntologyGraph graph, Atomic[] currentBinding, Map<Variable,Integer> bindingPositions);

    protected List<Atomic[]> complex(OWLReasoner reasoner, OWLDataFactory dataFactory, OWLOntologyGraph graph, Atomic[] currentBinding, Axiom axiom, Map<Variable,Integer> bindingPositions) {
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
    public abstract <O> O accept(QueryObjectVisitorEx<O> visitor);
    public String toString() {
        return m_axiomTemplate.toString();
    }
}
