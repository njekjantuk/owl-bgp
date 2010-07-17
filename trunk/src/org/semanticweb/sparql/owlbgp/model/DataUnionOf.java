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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.sparql.owlbgp.model.Variable.VarType;

public class DataUnionOf extends AbstractExtendedOWLObject implements DataRange {
    private static final long serialVersionUID = 2833631154707106474L;

    protected static InterningManager<DataUnionOf> s_interningManager=new InterningManager<DataUnionOf>() {
        protected boolean equal(DataUnionOf intersection1,DataUnionOf intersection2) {
            if (intersection1.m_dataRanges.size()!=intersection2.m_dataRanges.size())
                return false;
            for (DataRange conjunct : intersection1.m_dataRanges) {
                if (!contains(conjunct, intersection2.m_dataRanges))
                    return false;
            } 
            return true;
        }
        protected boolean contains(DataRange dataRange,Set<DataRange> dataRanges) {
            for (DataRange conjunct: dataRanges)
                if (conjunct==dataRange)
                    return true;
            return false;
        }
        protected int getHashCode(DataUnionOf intersection) {
            int hashCode=0;
            for (DataRange conjunct : intersection.m_dataRanges)
                hashCode+=conjunct.hashCode();
            return hashCode;
        }
    };
    
    protected final Set<DataRange> m_dataRanges;
    
    protected DataUnionOf(Set<DataRange> dataRanges) {
        m_dataRanges=dataRanges;
    }
    public  Set<DataRange> getDataRanges() {
        return m_dataRanges;
    }
    public String toString(Prefixes prefixes) {
        StringBuffer buffer=new StringBuffer();
        buffer.append("DataUnionOf(");
        boolean notFirst=false;
        for (DataRange conjunct : m_dataRanges) {
            if (notFirst)
                buffer.append(' ');
            else 
                notFirst=true;
            buffer.append(conjunct.toString(prefixes));
        }
        buffer.append(")");
        return buffer.toString();
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static DataUnionOf create(Set<DataRange> dataRanges) {
        return s_interningManager.intern(new DataUnionOf(dataRanges));
    }
    public static DataUnionOf create(DataRange... dataRanges) {
        return s_interningManager.intern(new DataUnionOf(new HashSet<DataRange>(Arrays.asList(dataRanges))));
    }
    public <O> O accept(ExtendedOWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
    protected OWLObject convertToOWLAPIObject(OWLAPIConverter converter) {
        return converter.visit(this);
    }
    public Set<Variable> getVariablesInSignature(VarType varType) {
        Set<Variable> variables=new HashSet<Variable>();
        for (DataRange dataRange : m_dataRanges) {
            variables.addAll(dataRange.getVariablesInSignature(varType));
        }
        return variables;
    }
    public Set<Variable> getUnboundVariablesInSignature(VarType varType) {
        Set<Variable> unbound=new HashSet<Variable>();
        for (DataRange dataRange : m_dataRanges) 
            unbound.addAll(dataRange.getUnboundVariablesInSignature(varType));
        return unbound;
    }
    public void applyBindings(Map<Variable,Atomic> variablesToBindings) {
        for (DataRange dataRange : m_dataRanges)
            dataRange.applyBindings(variablesToBindings);
    }
}