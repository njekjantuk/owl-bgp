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


public class ObjectMaxCardinality extends AbstractExtendedOWLObject implements ClassExpression {
    private static final long serialVersionUID = -5215709385051212705L;
    
    protected final int m_cardinality;
    protected final ObjectPropertyExpression m_ope;
    protected final ClassExpression m_classExpression;
   
    protected ObjectMaxCardinality(int cardinality,ObjectPropertyExpression ope,ClassExpression classExpression) {
        m_cardinality=cardinality;
        m_ope=ope;
        m_classExpression=classExpression;
    }
    public String toString(Prefixes prefixes) {
        StringBuffer buffer=new StringBuffer();
        buffer.append("ObjectMaxCardinality(");
        buffer.append(m_cardinality);
        buffer.append(" ");
        buffer.append(m_ope.toString(prefixes));
        buffer.append(" ");
        buffer.append(m_classExpression.toString(prefixes));
        buffer.append(")");
        return buffer.toString();
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    protected static InterningManager<ObjectMaxCardinality> s_interningManager=new InterningManager<ObjectMaxCardinality>() {
        protected boolean equal(ObjectMaxCardinality object1,ObjectMaxCardinality object2) {
            return object1.m_cardinality==object2.m_cardinality && object1.m_ope.equals(object2.m_ope) && object1.m_classExpression.equals(object2.m_classExpression);
        }
        protected int getHashCode(ObjectMaxCardinality object) {
            return object.m_cardinality*11+object.m_ope.hashCode()*23+object.m_classExpression.hashCode()*17;
        }
    };
    public static ObjectMaxCardinality create(int cardinality,ObjectPropertyExpression ope,ClassExpression classExpression) {
        return s_interningManager.intern(new ObjectMaxCardinality(cardinality,ope,classExpression));
    }
    public String getIdentifier() {
        return null;
    }
    public <O> O accept(ExtendedOWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
    public Set<Variable> getVariablesInSignature() {
        Set<Variable> variables=new HashSet<Variable>();
        variables.addAll(m_ope.getVariablesInSignature());
        variables.addAll(m_classExpression.getVariablesInSignature());
        return variables;
    }
}
