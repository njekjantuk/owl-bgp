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


public class DataAllValuesFrom extends AbstractExtendedOWLObject implements ClassExpression {
    private static final long serialVersionUID = -7542119517439032267L;
    
    protected static InterningManager<DataAllValuesFrom> s_interningManager=new InterningManager<DataAllValuesFrom>() {
        protected boolean equal(DataAllValuesFrom object1,DataAllValuesFrom object2) {
            return object1.m_dpe==object2.m_dpe && object1.m_dataRange==object2.m_dataRange;
        }
        protected int getHashCode(DataAllValuesFrom object) {
            return 11*object.m_dpe.hashCode()+23*object.m_dataRange.hashCode();
        }
    };
    
    protected final DataPropertyExpression m_dpe;
    protected final DataRange m_dataRange;
   
    protected DataAllValuesFrom(DataPropertyExpression ope,DataRange dataRange) {
        m_dpe=ope;
        m_dataRange=dataRange;
    }
    public String toString(Prefixes prefixes) {
        StringBuffer buffer=new StringBuffer();
        buffer.append("DataAllValuesFrom(");
        buffer.append(m_dpe.toString(prefixes));
        buffer.append(" ");
        buffer.append(m_dataRange.toString(prefixes));
        buffer.append(")");
        return buffer.toString();
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static DataAllValuesFrom create(DataPropertyExpression dpe,DataRange dataRange) {
        return s_interningManager.intern(new DataAllValuesFrom(dpe,dataRange));
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
        variables.addAll(m_dpe.getVariablesInSignature(varType));
        variables.addAll(m_dataRange.getVariablesInSignature(varType));
        return variables;
    }
    public Set<Variable> getUnboundVariablesInSignature(VarType varType) {
        Set<Variable> unbound=new HashSet<Variable>();
        unbound.addAll(m_dpe.getUnboundVariablesInSignature(varType));
        unbound.addAll(m_dataRange.getUnboundVariablesInSignature(varType));
        return unbound;
    }
    public void applyBindings(Map<String,String> variablesToBindings) {
        m_dpe.applyBindings(variablesToBindings);
        m_dataRange.applyBindings(variablesToBindings);
    }
    public void applyVariableBindings(Map<Variable,ExtendedOWLObject> variablesToBindings) {
        m_dpe.applyVariableBindings(variablesToBindings);
        m_dataRange.applyVariableBindings(variablesToBindings);
    }
}
