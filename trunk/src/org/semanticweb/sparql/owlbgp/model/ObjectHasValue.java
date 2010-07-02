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


public class ObjectHasValue extends AbstractExtendedOWLObject implements ClassExpression {
    private static final long serialVersionUID = -4900504105887302464L;

    protected static InterningManager<ObjectHasValue> s_interningManager=new InterningManager<ObjectHasValue>() {
        protected boolean equal(ObjectHasValue object1,ObjectHasValue object2) {
            return object1.m_ope==object2.m_ope&&object1.m_individual==object2.m_individual;
        }
        protected int getHashCode(ObjectHasValue object) {
            return 7*object.m_ope.hashCode()+23*object.m_individual.hashCode();
        }
    };
    
    protected final ObjectPropertyExpression m_ope;
    protected final Individual m_individual;
   
    protected ObjectHasValue(ObjectPropertyExpression ope,Individual individual) {
        m_ope=ope;
        m_individual=individual;
    }
    public ObjectPropertyExpression getObjectPropertyExpression() {
        return m_ope;
    }
    public String toString(Prefixes prefixes) {
        StringBuffer buffer=new StringBuffer();
        buffer.append("ObjectHasValue(");
        buffer.append(m_ope.toString(prefixes));
        buffer.append(" ");
        buffer.append(m_individual.toString(prefixes));
        buffer.append(")");
        return buffer.toString();
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static ObjectHasValue create(ObjectPropertyExpression ope,Individual individual) {
        return s_interningManager.intern(new ObjectHasValue(ope,individual));
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
        variables.addAll(m_individual.getVariablesInSignature(varType));
        return variables;
    }
    public Set<Variable> getUnboundVariablesInSignature(VarType varType) {
        Set<Variable> unbound=new HashSet<Variable>();
        unbound.addAll(m_ope.getUnboundVariablesInSignature(varType));
        return unbound;
    }
    public void applyBindings(Map<String,String> variablesToBindings) {
        m_ope.applyBindings(variablesToBindings);
        m_individual.applyBindings(variablesToBindings);
    }
    public void applyVariableBindings(Map<Variable,ExtendedOWLObject> variablesToBindings) {
        m_ope.applyVariableBindings(variablesToBindings);
        m_individual.applyVariableBindings(variablesToBindings);
    }
}
