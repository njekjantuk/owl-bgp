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

public class DataPropertyAssertion extends AbstractAxiom implements Assertion {
    private static final long serialVersionUID = -1973619534397838523L;

    protected static InterningManager<DataPropertyAssertion> s_interningManager=new InterningManager<DataPropertyAssertion>() {
        protected boolean equal(DataPropertyAssertion object1,DataPropertyAssertion object2) {
            return object1.m_dpe==object2.m_dpe&&object1.m_individual==object2.m_individual&&object1.m_literal==object2.m_literal;
        }
        protected int getHashCode(DataPropertyAssertion object) {
            return 13*object.m_dpe.hashCode()+17*object.m_individual.hashCode()+23*object.m_literal.hashCode();
        }
    };
    
    protected final DataPropertyExpression m_dpe;
    protected final Individual m_individual;
    protected final ILiteral m_literal;
   
    protected DataPropertyAssertion(DataProperty dpe,Individual individual,ILiteral literal) {
        m_dpe=dpe;
        m_individual=individual;
        m_literal=literal;
    }
    public DataPropertyExpression getDataPropertyExpression() {
        return m_dpe;
    }
    public Individual getIndividual() {
        return m_individual;
    }
    public ILiteral getLiteral() {
        return m_literal;
    }
    public String toString(Prefixes prefixes) {
        StringBuffer buffer=new StringBuffer();
        buffer.append("DataPropertyAssertion(");
        buffer.append(m_dpe.toString(prefixes));
        buffer.append(" ");
        buffer.append(m_individual.toString(prefixes));
        buffer.append(" ");
        buffer.append(m_literal.toString(prefixes));
        buffer.append(")");
        return buffer.toString();
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static DataPropertyAssertion create(DataProperty dpe,Individual individual,ILiteral literal) {
        return s_interningManager.intern(new DataPropertyAssertion(dpe,individual,literal));
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
        variables.addAll(m_individual.getVariablesInSignature(varType));
        variables.addAll(m_literal.getVariablesInSignature(varType));
        return variables;
    }
    public Set<Variable> getUnboundVariablesInSignature(VarType varType) {
        Set<Variable> unbound=new HashSet<Variable>();
        unbound.addAll(m_dpe.getUnboundVariablesInSignature(varType));
        unbound.addAll(m_individual.getUnboundVariablesInSignature(varType));
        unbound.addAll(m_literal.getUnboundVariablesInSignature(varType));
        return unbound;
    }
    public void applyBindings(Map<String,String> variablesToBindings) {
        m_dpe.applyBindings(variablesToBindings);
        m_individual.applyBindings(variablesToBindings);
        m_literal.applyBindings(variablesToBindings);
    }
    public void applyVariableBindings(Map<Variable,ExtendedOWLObject> variablesToBindings) {
        m_dpe.applyVariableBindings(variablesToBindings);
        m_individual.applyVariableBindings(variablesToBindings);
        m_literal.applyVariableBindings(variablesToBindings);
    }
}
