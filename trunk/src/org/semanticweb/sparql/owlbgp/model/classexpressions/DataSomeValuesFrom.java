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
package org.semanticweb.sparql.owlbgp.model.classexpressions;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.sparql.owlbgp.model.AbstractExtendedOWLObject;
import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.ExtendedOWLObject;
import org.semanticweb.sparql.owlbgp.model.ExtendedOWLObjectVisitorEx;
import org.semanticweb.sparql.owlbgp.model.InterningManager;
import org.semanticweb.sparql.owlbgp.model.OWLAPIConverter;
import org.semanticweb.sparql.owlbgp.model.Prefixes;
import org.semanticweb.sparql.owlbgp.model.Variable;
import org.semanticweb.sparql.owlbgp.model.Variable.VarType;
import org.semanticweb.sparql.owlbgp.model.dataranges.DataRange;
import org.semanticweb.sparql.owlbgp.model.properties.DataPropertyExpression;


public class DataSomeValuesFrom extends AbstractExtendedOWLObject implements ClassExpression {
    private static final long serialVersionUID = 1352023477502464017L;

    protected static InterningManager<DataSomeValuesFrom> s_interningManager=new InterningManager<DataSomeValuesFrom>() {
        protected boolean equal(DataSomeValuesFrom object1,DataSomeValuesFrom object2) {
            return object1.m_dpe==object2.m_dpe && object1.m_dataRange==object2.m_dataRange;
        }
        protected int getHashCode(DataSomeValuesFrom object) {
            return 17*object.m_dpe.hashCode()+7*object.m_dataRange.hashCode();
        }
    };
    
    protected final DataPropertyExpression m_dpe;
    protected final DataRange m_dataRange;
   
    protected DataSomeValuesFrom(DataPropertyExpression ope,DataRange dataRange) {
        m_dpe=ope;
        m_dataRange=dataRange;
    }
    public DataPropertyExpression getDataPropertyExpression() {
        return m_dpe;
    }
    public DataRange getDataRange() {
        return m_dataRange;
    }
    public String toString(Prefixes prefixes) {
        StringBuffer buffer=new StringBuffer();
        buffer.append("DataSomeValuesFrom(");
        buffer.append(m_dpe.toString(prefixes));
        buffer.append(" ");
        buffer.append(m_dataRange.toString(prefixes));
        buffer.append(")");
        return buffer.toString();
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static DataSomeValuesFrom create(DataPropertyExpression dpe,DataRange dataRange) {
        return s_interningManager.intern(new DataSomeValuesFrom(dpe,dataRange));
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
    public ExtendedOWLObject getBoundVersion(Map<Variable,Atomic> variablesToBindings) {
        return create((DataPropertyExpression)m_dpe.getBoundVersion(variablesToBindings),(DataRange)m_dataRange.getBoundVersion(variablesToBindings));
    }
}
