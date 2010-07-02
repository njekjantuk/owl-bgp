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


public class InverseObjectProperty extends AbstractExtendedOWLObject implements ObjectPropertyExpression {
    private static final long serialVersionUID = 4170522309299326290L;

    protected static InterningManager<InverseObjectProperty> s_interningManager=new InterningManager<InverseObjectProperty>() {
        protected boolean equal(InverseObjectProperty object1,InverseObjectProperty object2) {
            return object1.m_ope==object2.m_ope;
        }
        protected int getHashCode(InverseObjectProperty object) {
            return -object.m_ope.hashCode();
        }
    };
    
    protected final ObjectPropertyExpression m_ope;
   
    protected InverseObjectProperty(ObjectPropertyExpression objectPropertyExpression) {
        m_ope=objectPropertyExpression;
    }
    public ObjectPropertyExpression getInvertedObjectPropertyExpression() {
        return m_ope;
    }
    public String getIRIString() {
        return null;
    }
    public String toString(Prefixes prefixes) {
        return "ObjectInverseOf("+m_ope.toString(prefixes)+")";
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static InverseObjectProperty create(ObjectPropertyExpression objectPropertyExpression) {
        return s_interningManager.intern(new InverseObjectProperty(objectPropertyExpression));
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
        return variables;
    }
    public Set<Variable> getUnboundVariablesInSignature(VarType varType) {
        Set<Variable> unbound=new HashSet<Variable>();
        unbound.addAll(m_ope.getUnboundVariablesInSignature(varType));
        return unbound;
    }
    public void applyBindings(Map<String,String> variablesToBindings) {
        m_ope.applyBindings(variablesToBindings);
    }
    public void applyVariableBindings(Map<Variable,ExtendedOWLObject> variablesToBindings) {
        m_ope.applyVariableBindings(variablesToBindings);
    }
}
