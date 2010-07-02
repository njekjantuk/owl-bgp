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

public class DataOneOf extends AbstractExtendedOWLObject implements DataRange {
    private static final long serialVersionUID = 3341650510200806191L;

    protected static InterningManager<DataOneOf> s_interningManager=new InterningManager<DataOneOf>() {
        protected boolean equal(DataOneOf dataOneOf1,DataOneOf dataOneOf2) {
            if (dataOneOf1.m_enumeration.size()!=dataOneOf2.m_enumeration.size())
                return false;
            for (ILiteral literal : dataOneOf1.m_enumeration) {
                if (!contains(literal, dataOneOf2.m_enumeration))
                    return false;
            } 
            return true;
        }
        protected boolean contains(ILiteral literal ,Set<ILiteral> literals) {
            for (ILiteral oneOf : literals)
                if (literal==oneOf)
                    return true;
            return false;
        }
        protected int getHashCode(DataOneOf oneOf) {
            int hashCode=0;
            for (ILiteral literal : oneOf.m_enumeration)
                hashCode+=literal.hashCode();
            return hashCode;
        }
    };
    
    protected final Set<ILiteral> m_enumeration;
    
    protected DataOneOf(Set<ILiteral> enumeration) {
        m_enumeration=enumeration;
    }
    public Set<ILiteral> getLiterals() {
        return m_enumeration;
    }
    public String toString(Prefixes prefixes) {
        StringBuffer buffer=new StringBuffer();
        buffer.append("DataOneOf(");
        boolean notFirst=false;
        for (ILiteral literal : m_enumeration) {
            if (notFirst)
                buffer.append(' ');
            else 
                notFirst=true;
            buffer.append(literal.toString(prefixes));
        }
        buffer.append(")");
        return buffer.toString();
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static DataOneOf create(Set<ILiteral> oneOfs) {
        return s_interningManager.intern(new DataOneOf(oneOfs));
    }
    public static DataOneOf create(ILiteral... literals) {
        return s_interningManager.intern(new DataOneOf(new HashSet<ILiteral>(Arrays.asList(literals))));
    }
    public String getIdentifier() {
        return null;
    }
    public <O> O accept(ExtendedOWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
    protected OWLObject convertToOWLAPIObject(OWLAPIConverter converter) {
        return converter.visit(this);
    }
    public Set<Variable> getVariablesInSignature(VarType varType) {
        Set<Variable> variables=new HashSet<Variable>();
        for (ILiteral literal : m_enumeration) {
            variables.addAll(literal.getVariablesInSignature(varType));
        }
        return variables;
    }
    public Set<Variable> getUnboundVariablesInSignature(VarType varType) {
        Set<Variable> unbound=new HashSet<Variable>();
        for (ILiteral literal : m_enumeration) 
            unbound.addAll(literal.getUnboundVariablesInSignature(varType));
        return unbound;
    }
    public void applyBindings(Map<String,String> variablesToBindings) {
        for (ILiteral literal : m_enumeration)
            literal.applyBindings(variablesToBindings);
    }
    public void applyVariableBindings(Map<Variable,ExtendedOWLObject> variablesToBindings) {
        for (ILiteral literal : m_enumeration)
            literal.applyVariableBindings(variablesToBindings);
    }
}