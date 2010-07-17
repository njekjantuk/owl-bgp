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
            if (object1.m_dpe!=object2.m_dpe
                    ||object1.m_individual!=object2.m_individual
                    ||object1.m_literal!=object2.m_literal
                    ||object1.m_annotations.size()!=object2.m_annotations.size())
                return false;
            for (Annotation anno : object1.m_annotations) {
                if (!contains(anno, object2.m_annotations))
                    return false;
            } 
            return true;
        }
        protected boolean contains(Annotation annotation,Set<Annotation> annotations) {
            for (Annotation anno : annotations)
                if (anno==annotation)
                    return true;
            return false;
        }
        protected int getHashCode(DataPropertyAssertion object) {
            int hashCode=13*object.m_dpe.hashCode()+17*object.m_individual.hashCode()+23*object.m_literal.hashCode();
            for (Annotation anno : object.m_annotations)
                hashCode+=anno.hashCode();
            return hashCode;
        }
    };
    
    protected final DataPropertyExpression m_dpe;
    protected final Individual m_individual;
    protected final ILiteral m_literal;
   
    protected DataPropertyAssertion(DataProperty dpe,Individual individual,ILiteral literal,Set<Annotation> annotations) {
        m_dpe=dpe;
        m_individual=individual;
        m_literal=literal;
        m_annotations=annotations;
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
        writeAnnoations(buffer, prefixes);
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
    public static DataPropertyAssertion create(DataPropertyExpression dpe,Individual individual,ILiteral literal) {
        return DataPropertyAssertion.create(dpe, individual, literal,new HashSet<Annotation>());
    }
    public static DataPropertyAssertion create(DataPropertyExpression dpe,Individual individual,ILiteral literal,Set<Annotation> annotations) {
        if (!(dpe instanceof DataProperty)) throw new IllegalArgumentException("DatapropertyAssertions cannot contain data property expressions, but only data properties. Here we got an expression: "+dpe);
        return s_interningManager.intern(new DataPropertyAssertion((DataProperty)dpe,individual,literal,annotations));
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
        getAnnotationVariables(varType, variables);
        return variables;
    }
    public Set<Variable> getUnboundVariablesInSignature(VarType varType) {
        Set<Variable> unbound=new HashSet<Variable>();
        unbound.addAll(m_dpe.getUnboundVariablesInSignature(varType));
        unbound.addAll(m_individual.getUnboundVariablesInSignature(varType));
        unbound.addAll(m_literal.getUnboundVariablesInSignature(varType));
        getUnboundAnnotationVariables(varType, unbound);
        return unbound;
    }
    public void applyBindings(Map<Variable,Atomic> variablesToBindings) {
        m_dpe.applyBindings(variablesToBindings);
        m_individual.applyBindings(variablesToBindings);
        m_literal.applyBindings(variablesToBindings);
    }
    public Axiom getAxiomWithoutAnnotations() {
        return DataPropertyAssertion.create(m_dpe, m_individual, m_literal);
    }
}
