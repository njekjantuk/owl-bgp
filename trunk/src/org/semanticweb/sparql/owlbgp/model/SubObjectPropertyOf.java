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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.sparql.owlbgp.model.Variable.VarType;


public class SubObjectPropertyOf extends AbstractAxiom implements ObjectPropertyAxiom {
    private static final long serialVersionUID = -4739651270386976693L;

    protected static InterningManager<SubObjectPropertyOf> s_interningManager=new InterningManager<SubObjectPropertyOf>() {
        protected boolean equal(SubObjectPropertyOf object1,SubObjectPropertyOf object2) {
            return object1.m_subope==object2.m_subope&&object1.m_superope==object2.m_superope;
        }
        protected int getHashCode(SubObjectPropertyOf object) {
            return object.m_subope.hashCode()+11*object.m_superope.hashCode();
        }
    };
    
    protected final ObjectPropertyExpression m_subope;
    protected final ObjectPropertyExpression m_superope;
    
    protected SubObjectPropertyOf(ObjectPropertyExpression subObjectPropertyExpression,ObjectPropertyExpression superObjectPropertyExpression) {
        m_subope=subObjectPropertyExpression;
        m_superope=superObjectPropertyExpression;
    }
    public ObjectPropertyExpression getSubObjectPropertyExpression() {
        return m_subope;
    }
    public ObjectPropertyExpression getSuperObjectPropertyExpression() {
        return m_superope;
    }
    public String toString(Prefixes prefixes) {
        StringBuffer buffer=new StringBuffer();
        buffer.append("SubObjectPropertyOf(");
        buffer.append(m_subope.toString(prefixes));
        buffer.append(" ");
        buffer.append(m_superope.toString(prefixes));
        buffer.append(")");
        return buffer.toString();
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static SubObjectPropertyOf create(ObjectPropertyExpression subObjectPropertyExpression, ObjectPropertyExpression superObjectPropertyExpression) {
        return s_interningManager.intern(new SubObjectPropertyOf(subObjectPropertyExpression,superObjectPropertyExpression));
    }
    public <O> O accept(ExtendedOWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
    protected OWLObject convertToOWLAPIObject(OWLAPIConverter converter) {
        return converter.visit(this);
    }
    public Set<Variable> getVariablesInSignature(VarType varType) {
        Set<Variable> variables=new HashSet<Variable>();
        variables.addAll(m_subope.getVariablesInSignature(varType));
        variables.addAll(m_superope.getVariablesInSignature(varType));
        return variables;
    }
    public Set<Variable> getUnboundVariablesInSignature(VarType varType) {
        Set<Variable> unbound=new HashSet<Variable>();
        unbound.addAll(m_subope.getUnboundVariablesInSignature(varType));
        unbound.addAll(m_superope.getUnboundVariablesInSignature(varType));
        return unbound;
    }
    public void applyBindings(Map<String,String> variablesToBindings) {
        m_subope.applyBindings(variablesToBindings);
        m_superope.applyBindings(variablesToBindings);
    }
    public void applyVariableBindings(Map<Variable,ExtendedOWLObject> variablesToBindings) {
        m_subope.applyVariableBindings(variablesToBindings);
        m_superope.applyVariableBindings(variablesToBindings);
    }
}
