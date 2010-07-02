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

public class SubDataPropertyOf extends AbstractAxiom implements DataPropertyAxiom {
    private static final long serialVersionUID = 7386154464790495292L;

    protected static InterningManager<SubDataPropertyOf> s_interningManager=new InterningManager<SubDataPropertyOf>() {
        protected boolean equal(SubDataPropertyOf object1,SubDataPropertyOf object2) {
            return object1.m_subdpe==object2.m_subdpe&&object1.m_superdpe==object2.m_superdpe;
        }
        protected int getHashCode(SubDataPropertyOf object) {
            return 19*object.m_subdpe.hashCode()+7*object.m_superdpe.hashCode();
        }
    };
    
    protected final DataPropertyExpression m_subdpe;
    protected final DataPropertyExpression m_superdpe;
    
    protected SubDataPropertyOf(DataPropertyExpression subDataPropertyExpression,DataPropertyExpression superDataPropertyExpression) {
        m_subdpe=subDataPropertyExpression;
        m_superdpe=superDataPropertyExpression;
    }
    public DataPropertyExpression getSubDataPropertyExpression() {
        return m_subdpe;
    }
    public DataPropertyExpression getSuperDataPropertyExpression() {
        return m_superdpe;
    }
    public String toString(Prefixes prefixes) {
        StringBuffer buffer=new StringBuffer();
        buffer.append("SubDataPropertyOf(");
        buffer.append(m_subdpe.toString(prefixes));
        buffer.append(" ");
        buffer.append(m_superdpe.toString(prefixes));
        buffer.append(")");
        return buffer.toString();
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static SubDataPropertyOf create(DataPropertyExpression subDataPropertyExpression, DataPropertyExpression superDataPropertyExpression) {
        return s_interningManager.intern(new SubDataPropertyOf(subDataPropertyExpression,superDataPropertyExpression));
    }
    public <O> O accept(ExtendedOWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
    protected OWLObject convertToOWLAPIObject(OWLAPIConverter converter) {
        return converter.visit(this);
    }
    public Set<Variable> getVariablesInSignature(VarType varType) {
        Set<Variable> variables=new HashSet<Variable>();
        variables.addAll(m_subdpe.getVariablesInSignature(varType));
        variables.addAll(m_superdpe.getVariablesInSignature(varType));
        return variables;
    }
    public Set<Variable> getUnboundVariablesInSignature(VarType varType) {
        Set<Variable> unbound=new HashSet<Variable>();
        unbound.addAll(m_subdpe.getUnboundVariablesInSignature(varType));
        unbound.addAll(m_superdpe.getUnboundVariablesInSignature(varType));
        return unbound;
    }
    public void applyBindings(Map<String,String> variablesToBindings) {
        m_subdpe.applyBindings(variablesToBindings);
        m_superdpe.applyBindings(variablesToBindings);
    }
    public void applyVariableBindings(Map<Variable,ExtendedOWLObject> variablesToBindings) {
        m_subdpe.applyVariableBindings(variablesToBindings);
        m_superdpe.applyVariableBindings(variablesToBindings);
    }
}
