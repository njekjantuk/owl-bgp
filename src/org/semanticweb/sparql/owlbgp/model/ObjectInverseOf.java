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


public class ObjectInverseOf extends AbstractExtendedOWLObject implements ObjectPropertyExpression {
    private static final long serialVersionUID = 4170522309299326290L;

    protected static InterningManager<ObjectInverseOf> s_interningManager=new InterningManager<ObjectInverseOf>() {
        protected boolean equal(ObjectInverseOf object1,ObjectInverseOf object2) {
            return object1.m_op==object2.m_op;
        }
        protected int getHashCode(ObjectInverseOf object) {
            return -object.m_op.hashCode();
        }
    };
    
    protected final ObjectProperty m_op;
   
    protected ObjectInverseOf(ObjectProperty objectPropertyExpression) {
        m_op=objectPropertyExpression;
    }
    public ObjectProperty getInvertedObjectProperty() {
        return m_op;
    }
    public String getIRIString() {
        return null;
    }
    public String toString(Prefixes prefixes) {
        return "ObjectInverseOf("+m_op.toString(prefixes)+")";
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static ObjectInverseOf create(ObjectProperty objectProperty) {
        return s_interningManager.intern(new ObjectInverseOf(objectProperty));
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
        variables.addAll(m_op.getVariablesInSignature(varType));
        return variables;
    }
    public Set<Variable> getUnboundVariablesInSignature(VarType varType) {
        Set<Variable> unbound=new HashSet<Variable>();
        unbound.addAll(m_op.getUnboundVariablesInSignature(varType));
        return unbound;
    }
    public void applyBindings(Map<String,String> variablesToBindings) {
        m_op.applyBindings(variablesToBindings);
    }
    public void applyVariableBindings(Map<Variable,ExtendedOWLObject> variablesToBindings) {
        m_op.applyVariableBindings(variablesToBindings);
    }
}
