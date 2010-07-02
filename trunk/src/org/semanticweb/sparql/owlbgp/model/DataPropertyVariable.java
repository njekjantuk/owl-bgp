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


public class DataPropertyVariable extends Variable implements DataPropertyExpression {
    private static final long serialVersionUID = 3644196667162641608L;
    
    protected DataPropertyVariable(String variable) {
        super(variable);
    }
    public DataPropertyExpression getBindingAsExtendedOWLObject() {
        if (m_binding==null) return null;
        return DataProperty.create(m_binding);
    }
    public void setBinding(DataPropertyExpression binding) {
        m_binding=binding.getIdentifier();
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    protected static InterningManager<DataPropertyVariable> s_interningManager=new InterningManager<DataPropertyVariable>() {
        protected boolean equal(DataPropertyVariable object1,DataPropertyVariable object2) {
            return object1.m_variable.equals(object2.m_variable);
        }
        protected int getHashCode(DataPropertyVariable object) {
            return object.m_variable.hashCode();
        }
    };
    public static DataPropertyVariable create(String iri) {
        return s_interningManager.intern(new DataPropertyVariable(iri));
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
