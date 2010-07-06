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


public class ObjectPropertyAssertion extends AbstractAxiom implements Assertion {
    private static final long serialVersionUID = -1819707843103417338L;

    protected static InterningManager<ObjectPropertyAssertion> s_interningManager=new InterningManager<ObjectPropertyAssertion>() {
        protected boolean equal(ObjectPropertyAssertion object1,ObjectPropertyAssertion object2) {
            return object1.m_ope==object2.m_ope&&object1.m_individual1==object2.m_individual1&&object1.m_individual2==object2.m_individual2;
        }
        protected int getHashCode(ObjectPropertyAssertion object) {
            return 7*object.m_ope.hashCode()+11*object.m_individual1.hashCode()+53*object.m_individual2.hashCode();
        }
    };
    
    protected final ObjectPropertyExpression m_ope;
    protected final Individual m_individual1;
    protected final Individual m_individual2;
   
    protected ObjectPropertyAssertion(ObjectProperty ope,Individual individual1,Individual individual2) {
        m_ope=ope;
        m_individual1=individual1;
        m_individual2=individual2;
    }
    public ObjectPropertyExpression getObjectPropertyExpression() {
        return m_ope;
    }
    public Individual getIndividual1() {
        return m_individual1;
    }
    public Individual getSubject() {
        return m_individual1;
    }
    public Individual getIndividual2() {
        return m_individual2;
    }
    public Individual getobject() {
        return m_individual2;
    }
    public String toString(Prefixes prefixes) {
        StringBuffer buffer=new StringBuffer();
        buffer.append("ObjectPropertyAssertion(");
        buffer.append(m_ope.toString(prefixes));
        buffer.append(" ");
        buffer.append(m_individual1.toString(prefixes));
        buffer.append(" ");
        buffer.append(m_individual2.toString(prefixes));
        buffer.append(")");
        return buffer.toString();
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static ObjectPropertyAssertion create(ObjectProperty ope,Individual individual1,Individual individual2) {
        return s_interningManager.intern(new ObjectPropertyAssertion(ope,individual1,individual2));
    }
    public <O> O accept(ExtendedOWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
    protected OWLObject convertToOWLAPIObject(OWLAPIConverter converter) {
        return converter.visit(this);
    }
    public Set<Variable> getVariablesInSignature(VarType varType) {
        Set<Variable> variables=new HashSet<Variable>();
        variables.addAll(m_ope.getVariablesInSignature(varType));
        variables.addAll(m_individual1.getVariablesInSignature(varType));
        variables.addAll(m_individual2.getVariablesInSignature(varType));
        return variables;
    }
    public Set<Variable> getUnboundVariablesInSignature(VarType varType) {
        Set<Variable> unbound=new HashSet<Variable>();
        unbound.addAll(m_ope.getUnboundVariablesInSignature(varType));
        unbound.addAll(m_individual1.getUnboundVariablesInSignature(varType));
        unbound.addAll(m_individual2.getUnboundVariablesInSignature(varType));
        return unbound;
    }
    public void applyBindings(Map<String,String> variablesToBindings) {
        m_ope.applyBindings(variablesToBindings);
        m_individual1.applyBindings(variablesToBindings);
        m_individual2.applyBindings(variablesToBindings);
    }
    public void applyVariableBindings(Map<Variable,ExtendedOWLObject> variablesToBindings) {
        m_ope.applyVariableBindings(variablesToBindings);
        m_individual1.applyVariableBindings(variablesToBindings);
        m_individual2.applyVariableBindings(variablesToBindings);
    }
}
