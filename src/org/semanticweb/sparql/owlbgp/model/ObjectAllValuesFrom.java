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

public class ObjectAllValuesFrom extends AbstractExtendedOWLObject implements ClassExpression {
    private static final long serialVersionUID = -4230694140155179867L;

    protected static InterningManager<ObjectAllValuesFrom> s_interningManager=new InterningManager<ObjectAllValuesFrom>() {
        protected boolean equal(ObjectAllValuesFrom object1,ObjectAllValuesFrom object2) {
            return object1.m_ope==object2.m_ope && object1.m_classExpression==object2.m_classExpression;
        }
        protected int getHashCode(ObjectAllValuesFrom object) {
            return 7*object.m_ope.hashCode()+13*object.m_classExpression.hashCode();
        }
    };
    
    protected final ObjectPropertyExpression m_ope;
    protected final ClassExpression m_classExpression;
   
    protected ObjectAllValuesFrom(ObjectPropertyExpression ope,ClassExpression classExpression) {
        m_ope=ope;
        m_classExpression=classExpression;
    }
    public ObjectPropertyExpression getObjectPropertyExpression() {
        return m_ope;
    }
    public ClassExpression getClassExpression() {
        return m_classExpression;
    }
    public String toString(Prefixes prefixes) {
        StringBuffer buffer=new StringBuffer();
        buffer.append("ObjectAllValuesFrom(");
        buffer.append(m_ope.toString(prefixes));
        buffer.append(" ");
        buffer.append(m_classExpression.toString(prefixes));
        buffer.append(")");
        return buffer.toString();
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static ObjectAllValuesFrom create(ObjectPropertyExpression ope,ClassExpression classExpression) {
        return s_interningManager.intern(new ObjectAllValuesFrom(ope,classExpression));
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
        variables.addAll(m_ope.getVariablesInSignature(varType));
        variables.addAll(m_classExpression.getVariablesInSignature(varType));
        return variables;
    }
    public Set<Variable> getUnboundVariablesInSignature(VarType varType) {
        Set<Variable> unbound=new HashSet<Variable>();
        unbound.addAll(m_ope.getUnboundVariablesInSignature(varType));
        unbound.addAll(m_classExpression.getUnboundVariablesInSignature(varType));
        return unbound;
    }
    public void applyBindings(Map<String,String> variablesToBindings) {
        m_ope.applyBindings(variablesToBindings);
        m_classExpression.applyBindings(variablesToBindings);
    }
    public void applyVariableBindings(Map<Variable,ExtendedOWLObject> variablesToBindings) {
        m_ope.applyVariableBindings(variablesToBindings);
        m_classExpression.applyVariableBindings(variablesToBindings);
    }
}
