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


public class IndividualVariable extends Variable implements Individual {
    private static final long serialVersionUID = 7157759527436797847L;

    protected static InterningManager<IndividualVariable> s_interningManager=new InterningManager<IndividualVariable>() {
        protected boolean equal(IndividualVariable object1,IndividualVariable object2) {
            return object1.m_variable==object2.m_variable;
        }
        protected int getHashCode(IndividualVariable object) {
            return object.m_variable.hashCode();
        }
    };
    
    protected IndividualVariable(String variable) {
        super(variable);
    }
    public Individual getBindingAsExtendedOWLObject() {
        if (m_binding==null) return null;
        if (m_binding.startsWith("_:")) return AnonymousIndividual.create(m_binding);
        else return NamedIndividual.create(m_binding);
    }
    public void setBinding(Individual binding) {
        if (binding==null) m_binding=null;
        else m_binding=binding.getIdentifier();
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static IndividualVariable create(String iri) {
        return s_interningManager.intern(new IndividualVariable(iri));
    }
    public <O> O accept(ExtendedOWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
    protected OWLObject convertToOWLAPIObject(OWLAPIConverter converter) {
        return converter.visit(this);
    }
    public Set<Variable> getVariablesInSignature(VarType varType) {
        Set<Variable> variables=new HashSet<Variable>();
        if (varType==null||varType==VarType.INDIVIDUAL) variables.add(this);
        return variables;
    }
    public Set<Variable> getUnboundVariablesInSignature(VarType varType) {
        Set<Variable> variables=new HashSet<Variable>();
        if (m_binding==null&&(varType==null||varType==VarType.INDIVIDUAL)) variables.add(this);
        return variables;
    }
    public void applyVariableBindings(Map<Variable,ExtendedOWLObject> variablesToBindings) {
        ExtendedOWLObject binding=variablesToBindings.get(this);
        if (binding==null)
            m_binding=null;
        else if (!(binding instanceof Individual))
            throw new RuntimeException("Error: Only individuals can be assigned to individual variables, but individual variable "+m_variable+" was assigned the non-individual "+binding);
        else 
            m_binding=((Individual)variablesToBindings.get(this)).getIdentifier();
    }
}
