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

public class FunctionalDataProperty extends AbstractAxiom implements DataPropertyAxiom {
    private static final long serialVersionUID = -1638368948955304491L;

    protected static InterningManager<FunctionalDataProperty> s_interningManager=new InterningManager<FunctionalDataProperty>() {
        protected boolean equal(FunctionalDataProperty object1,FunctionalDataProperty object2) {
            return object1.m_dpe==object2.m_dpe;
        }
        protected int getHashCode(FunctionalDataProperty object) {
            return 17+13*object.m_dpe.hashCode();
        }
    };
    
    protected final DataPropertyExpression m_dpe;
   
    protected FunctionalDataProperty(DataPropertyExpression dataPropertyExpression) {
        m_dpe=dataPropertyExpression;
    }
    public DataPropertyExpression getDataPropertyExpression() {
        return m_dpe;
    }
    public String toString(Prefixes prefixes) {
        return "FunctionalDataProperty("+m_dpe.toString(prefixes)+")";
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static FunctionalDataProperty create(DataPropertyExpression dataPropertyExpression) {
        return s_interningManager.intern(new FunctionalDataProperty(dataPropertyExpression));
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
        return variables;
    }
    public Set<Variable> getUnboundVariablesInSignature(VarType varType) {
        Set<Variable> unbound=new HashSet<Variable>();
        unbound.addAll(m_dpe.getUnboundVariablesInSignature(varType));
        return unbound;
    }
    public void applyBindings(Map<String,String> variablesToBindings) {
        m_dpe.applyBindings(variablesToBindings);
    }
    public void applyVariableBindings(Map<Variable,ExtendedOWLObject> variablesToBindings) {
        m_dpe.applyVariableBindings(variablesToBindings);
    }
}
