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
import java.util.Set;


public class DataHasValue extends AbstractExtendedOWLObject implements ClassExpression {
    private static final long serialVersionUID = 584771936735129139L;

    protected final DataPropertyExpression m_dpe;
    protected final ILiteral m_literal;
   
    protected DataHasValue(DataPropertyExpression dpe,ILiteral literal) {
        m_dpe=dpe;
        m_literal=literal;
    }
    public String toString(Prefixes prefixes) {
        StringBuffer buffer=new StringBuffer();
        buffer.append("DataHasValue(");
        buffer.append(m_dpe.toString(prefixes));
        buffer.append(" ");
        buffer.append(m_literal.toString(prefixes));
        buffer.append(")");
        return buffer.toString();
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    protected static InterningManager<DataHasValue> s_interningManager=new InterningManager<DataHasValue>() {
        protected boolean equal(DataHasValue object1,DataHasValue object2) {
            return object1.m_dpe.equals(object2.m_dpe) && object1.m_literal.equals(object2.m_literal);
        }
        protected int getHashCode(DataHasValue object) {
            return 11*object.m_dpe.hashCode()+17*object.m_literal.hashCode();
        }
    };
    public static DataHasValue create(DataPropertyExpression dpe,ILiteral literal) {
        return s_interningManager.intern(new DataHasValue(dpe,literal));
    }
    public String getIdentifier() {
        return null;
    }
    public <O> O accept(ExtendedOWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
    public Set<Variable> getVariablesInSignature() {
        Set<Variable> variables=new HashSet<Variable>();
        variables.addAll(m_dpe.getVariablesInSignature());
        variables.addAll(m_literal.getVariablesInSignature());
        return variables;
    }
}
