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


public class IndividualVariable extends Variable implements Individual {
    private static final long serialVersionUID = 7157759527436797847L;
    
    protected IndividualVariable(String variable) {
        super(variable);
    }
    public Individual getBindingAsExtendedOWLObject() {
        if (m_binding==null) return null;
        if (m_binding.startsWith("_:")) return AnonymousIndividual.create(m_binding);
        else return NamedIndividual.create(m_binding);
    }
    public void setBinding(Individual binding) {
        m_binding=binding.getIdentifier();
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    protected static InterningManager<IndividualVariable> s_interningManager=new InterningManager<IndividualVariable>() {
        protected boolean equal(IndividualVariable object1,IndividualVariable object2) {
            return object1.m_variable.equals(object2.m_variable);
        }
        protected int getHashCode(IndividualVariable object) {
            return object.m_variable.hashCode();
        }
    };
    public static IndividualVariable create(String iri) {
        return s_interningManager.intern(new IndividualVariable(iri));
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
