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


public class ClassVariable extends Variable implements ClassExpression {
    private static final long serialVersionUID = -4710499264999865534L;
    
    protected ClassVariable(String variable) {
        super(variable);
    }
    public ClassExpression getBindingAsExtendedOWLObject() {
        if (m_binding==null) return null;
        return Clazz.create(m_binding);
    }
    public void setBinding(ClassExpression binding) {
        m_binding=binding.getIdentifier();
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    protected static InterningManager<ClassVariable> s_interningManager=new InterningManager<ClassVariable>() {
        protected boolean equal(ClassVariable object1,ClassVariable object2) {
            return object1.m_variable.equals(object2.m_variable);
        }
        protected int getHashCode(ClassVariable object) {
            return object.m_variable.hashCode();
        }
    };
    public static ClassVariable create(String iri) {
        return s_interningManager.intern(new ClassVariable(iri));
    }
    public <O> O accept(ExtendedOWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
    public Set<Variable> getVariablesInSignature() {
        Set<Variable> variables=new HashSet<Variable>();
        variables.add(this);
        return variables;
    }
}
