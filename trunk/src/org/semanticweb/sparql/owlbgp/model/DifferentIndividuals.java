/* Copyright 2010 by the Oxford University Computing Laboratory
   
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
   along with OWL-BGP.  If not, see <http://www.gnu.org/licenses/>.
*/
package org.semanticweb.sparql.owlbgp.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.sparql.owlbgp.model.Variable.VarType;


public class DifferentIndividuals extends AbstractAxiom implements Assertion {
    private static final long serialVersionUID = -1177589453735862066L;

    protected static InterningManager<DifferentIndividuals> s_interningManager=new InterningManager<DifferentIndividuals>() {
        protected boolean equal(DifferentIndividuals object1,DifferentIndividuals object2) {
            if (object1.m_individuals.size()!=object2.m_individuals.size())
                return false;
            for (Individual individual : object1.m_individuals) {
                if (!contains(individual, object2.m_individuals))
                    return false;
            } 
            return true;
        }
        protected boolean contains(Individual individual ,Set<Individual> individuals) {
            for (Individual ind : individuals)
                if (individual==ind)
                    return true;
            return false;
        }
        protected int getHashCode(DifferentIndividuals object) {
            int hashCode=127;
            for (Individual individual : object.m_individuals)
                hashCode+=individual.hashCode();
            return hashCode;
        }
    };
    
    protected final Set<Individual> m_individuals;
   
    protected DifferentIndividuals(Individual... individuals) {
        m_individuals=new HashSet<Individual>(Arrays.asList(individuals));
    }
    protected DifferentIndividuals(Collection<Individual> individuals) {
        m_individuals=new HashSet<Individual>(individuals);
    }
    public Set<Individual> getIndividuals() {
        return m_individuals;
    }
    public String toString(Prefixes prefixes) {
        StringBuffer buffer=new StringBuffer();
        buffer.append("DifferentIndividuals(");
        boolean notFirst=false;
        for (Individual individual : m_individuals) {
            if (notFirst)
                buffer.append(' ');
            else 
                notFirst=true;
            buffer.append(individual.toString(prefixes));
        }
        buffer.append(")");
        return buffer.toString();
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static DifferentIndividuals create(Set<Individual> individuals) {
        return s_interningManager.intern(new DifferentIndividuals(individuals));
    }
    public static DifferentIndividuals create(Individual... individuals) {
        return s_interningManager.intern(new DifferentIndividuals(individuals));
    }
    public <O> O accept(ExtendedOWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
    protected OWLObject convertToOWLAPIObject(OWLAPIConverter converter) {
        return converter.visit(this);
    }
    public Set<Variable> getVariablesInSignature(VarType varType) {
        Set<Variable> variables=new HashSet<Variable>();
        for (Individual individual : m_individuals) {
            variables.addAll(individual.getVariablesInSignature(varType));
        }
        return variables;
    }
    public Set<Variable> getUnboundVariablesInSignature(VarType varType) {
        Set<Variable> unbound=new HashSet<Variable>();
        for (Individual individual : m_individuals) 
            unbound.addAll(individual.getUnboundVariablesInSignature(varType));
        return unbound;
    }
    public void applyBindings(Map<String,String> variablesToBindings) {
        for (Individual individual : m_individuals)
            individual.applyBindings(variablesToBindings);
    }
    public void applyVariableBindings(Map<Variable,ExtendedOWLObject> variablesToBindings) {
        for (Individual individual : m_individuals)
            individual.applyVariableBindings(variablesToBindings);
    }
}
