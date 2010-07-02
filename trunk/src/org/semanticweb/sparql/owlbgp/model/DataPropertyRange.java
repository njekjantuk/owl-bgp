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

public class DataPropertyRange extends AbstractAxiom implements DataPropertyAxiom {
    private static final long serialVersionUID = -2081702943726360480L;

    protected static InterningManager<DataPropertyRange> s_interningManager=new InterningManager<DataPropertyRange>() {
        protected boolean equal(DataPropertyRange object1,DataPropertyRange object2) {
            return object1.m_dpe==object2.m_dpe && object1.m_dataRange==object2.m_dataRange;
        }
        protected int getHashCode(DataPropertyRange object) {
            return 11+133*object.m_dpe.hashCode()+5*object.m_dataRange.hashCode();
        }
    };
    
    protected final DataPropertyExpression m_dpe;
    protected final DataRange m_dataRange;
   
    protected DataPropertyRange(DataPropertyExpression dpe,DataRange dataRange) {
        m_dpe=dpe;
        m_dataRange=dataRange;
    }
    public DataPropertyExpression getDataPropertyExpression() {
        return m_dpe;
    }
    public DataRange getRange() {
        return m_dataRange;
    }
    public String toString(Prefixes prefixes) {
        StringBuffer buffer=new StringBuffer();
        buffer.append("DataPropertyRange(");
        buffer.append(m_dpe.toString(prefixes));
        buffer.append(" ");
        buffer.append(m_dataRange.toString(prefixes));
        buffer.append(")");
        return buffer.toString();
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static DataPropertyRange create(DataPropertyExpression dpe,DataRange dataRange) {
        return s_interningManager.intern(new DataPropertyRange(dpe,dataRange));
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
