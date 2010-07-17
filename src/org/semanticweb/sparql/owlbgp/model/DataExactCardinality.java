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


public class DataExactCardinality extends AbstractExtendedOWLObject implements ClassExpression {
    private static final long serialVersionUID = -7848221781154806956L;
    
    protected static InterningManager<DataExactCardinality> s_interningManager=new InterningManager<DataExactCardinality>() {
        protected boolean equal(DataExactCardinality object1,DataExactCardinality object2) {
            return object1.m_cardinality==object2.m_cardinality && object1.m_dpe==object2.m_dpe && object1.m_dataRange==object2.m_dataRange;
        }
        protected int getHashCode(DataExactCardinality object) {
            return 23*object.m_cardinality+17*object.m_dpe.hashCode()+7*object.m_dataRange.hashCode();
        }
    };
    
    protected final int m_cardinality;
    protected final DataPropertyExpression m_dpe;
    protected final DataRange m_dataRange;
   
    protected DataExactCardinality(int cardinality,DataPropertyExpression ope,DataRange dataRange) {
        m_cardinality=cardinality;
        m_dpe=ope;
        m_dataRange=dataRange;
    }
    public int getCardinality() {
        return m_cardinality;
    }
    public DataPropertyExpression getDataPropertyExpression() {
        return m_dpe;
    }
    public DataRange getDataRange() {
        return m_dataRange;
    }
    public String toString(Prefixes prefixes) {
        StringBuffer buffer=new StringBuffer();
        buffer.append("DataExactCardinality(");
        buffer.append(m_cardinality);
        buffer.append(" ");
        buffer.append(m_dpe.toString(prefixes));
        buffer.append(" ");
        buffer.append(m_dataRange.toString(prefixes));
        buffer.append(")");
        return buffer.toString();
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static DataExactCardinality create(int cardinality,DataPropertyExpression dpe,DataRange dataRange) {
        return s_interningManager.intern(new DataExactCardinality(cardinality,dpe,dataRange));
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
    public void applyBindings(Map<Variable,Atomic> variablesToBindings) {
        m_dpe.applyBindings(variablesToBindings);
        m_dataRange.applyBindings(variablesToBindings);
    }
}
