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


public class ObjectHasSelf extends AbstractExtendedOWLObject implements ClassExpression {
    private static final long serialVersionUID = -5958845591224826209L;
    
    protected final ObjectPropertyExpression m_ope;
    
    protected ObjectHasSelf(ObjectPropertyExpression ope) {
        m_ope=ope;
    }
    public String toString(Prefixes prefixes) {
        StringBuffer buffer=new StringBuffer();
        buffer.append("ObjectHasSelf(");
        buffer.append(m_ope.toString(prefixes));
        buffer.append(")");
        return buffer.toString();
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    protected static InterningManager<ObjectHasSelf> s_interningManager=new InterningManager<ObjectHasSelf>() {
        protected boolean equal(ObjectHasSelf object1,ObjectHasSelf object2) {
            return object1.m_ope.equals(object2.m_ope);
        }
        protected int getHashCode(ObjectHasSelf object) {
            return 17*object.m_ope.hashCode();
        }
    };
    public static ObjectHasSelf create(ObjectPropertyExpression ope) {
        return s_interningManager.intern(new ObjectHasSelf(ope));
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
        return variables;
    }
}
