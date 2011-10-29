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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.sparql.arq.OWLOntologyGraph;
import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.BindingIterator;
import org.semanticweb.sparql.owlbgp.model.ToOWLAPIConverter;
import org.semanticweb.sparql.owlbgp.model.Variable;
import org.semanticweb.sparql.owlbgp.model.axioms.Axiom;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassVariable;
import org.semanticweb.sparql.owlbgp.model.dataranges.DatatypeVariable;
import org.semanticweb.sparql.owlbgp.model.individuals.IndividualVariable;
import org.semanticweb.sparql.owlbgp.model.literals.LiteralVariable;
import org.semanticweb.sparql.owlbgp.model.properties.AnnotationPropertyVariable;
import org.semanticweb.sparql.owlbgp.model.properties.DataPropertyVariable;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyVariable;

public abstract class AbstractQueryObject<T extends Axiom> implements QueryObject<T> {
    
    protected final ToOWLAPIConverter m_toOWLAPIConverter;
    protected final OWLOntologyGraph m_graph;
    protected final OWLReasoner m_reasoner;
    protected final OWLDataFactory m_dataFactory;
    protected final T m_axiomTemplate;
    
    public AbstractQueryObject(T axiomTemplate, OWLOntologyGraph graph) {
        m_graph=graph;
        m_reasoner=m_graph.getReasoner();
        m_dataFactory=m_reasoner.getRootOntology().getOWLOntologyManager().getOWLDataFactory();
        m_toOWLAPIConverter=new ToOWLAPIConverter(m_dataFactory);
        m_axiomTemplate=axiomTemplate;
    }
    @Override
    public T getAxiomTemplate() {
        return m_axiomTemplate;
    }
    public List<Atomic[]> computeBindings(List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions) {
        // if no solutions are computed yet, candidate bindings should have one all null array as an entry 
        // if candidateBindings is empty, there are no solutions already due to other constraints
        if (candidateBindings.size()==0)
            return candidateBindings;
        
        List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
        for (int i=0;i<candidateBindings.size();i++)
            newBindings.addAll(addBindings(candidateBindings.get(i), bindingPositions));
        return newBindings;
    }
    protected abstract List<Atomic[]> addBindings(Atomic[] currentBinding, Map<Variable,Integer> bindingPositions);

    protected List<Atomic[]> complex(Atomic[] currentBinding, Axiom axiom, Map<Variable,Integer> bindingPositions) {
        List<Atomic[]> results=new ArrayList<Atomic[]>();
        Atomic[] result;
        List<Variable> vars=new ArrayList<Variable>(axiom.getVariablesInSignature());
        Map<Variable,Set<? extends Atomic>> varToBindingSets=new HashMap<Variable, Set<? extends Atomic>>();
        for (Variable var : vars) {
            if (var instanceof ClassVariable)
                varToBindingSets.put(var, m_graph.getClassesInSignature());
            else if (var instanceof DatatypeVariable)
                varToBindingSets.put(var, m_graph.getDatatypesInSignature());
            else if (var instanceof ObjectPropertyVariable)
                varToBindingSets.put(var, m_graph.getObjectPropertiesInSignature());
            else if (var instanceof DataPropertyVariable)
                varToBindingSets.put(var, m_graph.getDataPropertiesInSignature());
            else if (var instanceof AnnotationPropertyVariable)
                varToBindingSets.put(var, m_graph.getAnnotationPropertiesInSignature());
            else if (var instanceof IndividualVariable)
                varToBindingSets.put(var, m_graph.getIndividualsInSignature());
            else if (var instanceof LiteralVariable)
                varToBindingSets.put(var, m_graph.getLiteralsInSignature());
            else 
                throw new IllegalArgumentException("Error: The class assertion axiom template "+axiom+" contains untyped variables. ");
        }
        for (Map<Variable,? extends Atomic> bindings : new BindingIterator(varToBindingSets)) {
            if (m_reasoner.isEntailed((OWLAxiom)axiom.getBoundVersion(bindings, m_dataFactory))) {
                result=currentBinding.clone();
                for (Variable var : bindings.keySet())
                    result[bindingPositions.get(var)]=bindings.get(var);
                results.add(result);
            }
        }
        return results;
    }
    public abstract <O> O accept(QueryObjectVisitorEx<O> visitor, Set<Variable> bound);
    public abstract <O> O accept(QueryObjectVisitorEx<O> visitor);
    public String toString() {
        return m_axiomTemplate.toString();
    }
}
