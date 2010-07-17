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


public class ClassAssertion extends AbstractAxiom implements Assertion {
    private static final long serialVersionUID = -4207422378867470495L;

    protected static InterningManager<ClassAssertion> s_interningManager=new InterningManager<ClassAssertion>() {
        protected boolean equal(ClassAssertion object1,ClassAssertion object2) {
            if (object1.m_ce!=object2.m_ce
                    ||object1.m_individual!=object2.m_individual
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
        protected int getHashCode(ClassAssertion object) {
            int hashCode=43*object.m_ce.hashCode()+7*object.m_individual.hashCode();
            for (Annotation anno : object.m_annotations)
                hashCode+=anno.hashCode();
            return hashCode;
        }
    };
    
    protected final ClassExpression m_ce;
    protected final Individual m_individual;
   
    protected ClassAssertion(ClassExpression ope,Individual individual,Set<Annotation> annotations) {
        m_ce=ope;
        m_individual=individual;
        m_annotations=annotations;
    }
    public ClassExpression getClassExpression() {
        return m_ce;
    }
    public Individual getIndividual() {
        return m_individual;
    }
    public String toString(Prefixes prefixes) {
        StringBuffer buffer=new StringBuffer();
        buffer.append("ClassAssertion(");
        writeAnnoations(buffer, prefixes);
        buffer.append(m_ce.toString(prefixes));
        buffer.append(" ");
        buffer.append(m_individual.toString(prefixes));
        buffer.append(")");
        return buffer.toString();
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static ClassAssertion create(ClassExpression ce,Individual individual) {
        return ClassAssertion.create(ce,individual,new HashSet<Annotation>());
    }
    public static ClassAssertion create(ClassExpression ce,Individual individual,Set<Annotation> annotations) {
        return s_interningManager.intern(new ClassAssertion(ce,individual,annotations));
    }
    public <O> O accept(ExtendedOWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
    protected OWLObject convertToOWLAPIObject(OWLAPIConverter converter) {
        return converter.visit(this);
    }
    public Set<Variable> getVariablesInSignature(VarType varType) {
        Set<Variable> variables=new HashSet<Variable>();
        variables.addAll(m_ce.getVariablesInSignature(varType));
        variables.addAll(m_individual.getVariablesInSignature(varType));
        getAnnotationVariables(varType, variables);
        return variables;
    }
    public Set<Variable> getUnboundVariablesInSignature(VarType varType) {
        Set<Variable> unbound=new HashSet<Variable>();
        unbound.addAll(m_ce.getUnboundVariablesInSignature(varType));
        unbound.addAll(m_individual.getUnboundVariablesInSignature(varType));
        getUnboundAnnotationVariables(varType, unbound);
        return unbound;
    }
    public void applyBindings(Map<Variable,Atomic> variablesToBindings) {
        m_ce.applyBindings(variablesToBindings);
        m_individual.applyBindings(variablesToBindings);
    }
    public Axiom getAxiomWithoutAnnotations() {
        return ClassAssertion.create(m_ce, m_individual);
    }
}
