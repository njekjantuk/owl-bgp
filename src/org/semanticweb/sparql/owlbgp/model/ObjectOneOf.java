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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.sparql.owlbgp.model.Variable.VarType;

public class ObjectOneOf extends AbstractExtendedOWLObject implements ClassExpression {
    private static final long serialVersionUID = -3492317792968371893L;

    protected static InterningManager<ObjectOneOf> s_interningManager=new InterningManager<ObjectOneOf>() {
        protected boolean equal(ObjectOneOf objectOneOf1,ObjectOneOf objectOneOf2) {
            if (objectOneOf1.m_enumeration.size()!=objectOneOf2.m_enumeration.size())
                return false;
            for (Individual individual : objectOneOf1.m_enumeration) {
                if (!contains(individual, objectOneOf2.m_enumeration))
                    return false;
            } 
            return true;
        }
        protected boolean contains(Individual individual ,Set<Individual> individuals) {
            for (Individual oneOf : individuals)
                if (individual==oneOf)
                    return true;
            return false;
        }
        protected int getHashCode(ObjectOneOf oneOf) {
            int hashCode=0;
            for (Individual individual : oneOf.m_enumeration)
                hashCode+=individual.hashCode();
            return hashCode;
        }
    };
    
    protected final Set<Individual> m_enumeration;
    
    protected ObjectOneOf(Set<Individual> enumeration) {
        m_enumeration=enumeration;
    }
    public Set<Individual> getIndividuals() {
        return m_enumeration;
    }
    public String toString(Prefixes prefixes) {
        StringBuffer buffer=new StringBuffer();
        buffer.append("ObjectOneOf(");
        boolean notFirst=false;
        for (Individual individual : m_enumeration) {
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
    public static ObjectOneOf create(Set<Individual> oneOfs) {
        return s_interningManager.intern(new ObjectOneOf(oneOfs));
    }
    public static ObjectOneOf create(Individual... individuals) {
        return s_interningManager.intern(new ObjectOneOf(new HashSet<Individual>(Arrays.asList(individuals))));
    }
    public <O> O accept(ExtendedOWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
    protected OWLObject convertToOWLAPIObject(OWLAPIConverter converter) {
        return converter.visit(this);
    }
    public Set<Variable> getVariablesInSignature(VarType varType) {
        Set<Variable> variables=new HashSet<Variable>();
        for (Individual individual : m_enumeration) {
            variables.addAll(individual.getVariablesInSignature(varType));
        }
        return variables;
    }
    public Set<Variable> getUnboundVariablesInSignature(VarType varType) {
        Set<Variable> unbound=new HashSet<Variable>();
        for (Individual individual : m_enumeration) 
            unbound.addAll(individual.getUnboundVariablesInSignature(varType));
        return unbound;
    }
    public void applyBindings(Map<Variable,Atomic> variablesToBindings) {
        for (Individual individual : m_enumeration)
            individual.applyBindings(variablesToBindings);
    }
}