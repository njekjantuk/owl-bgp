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
package org.semanticweb.sparql.owlbgp.model.axioms;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.sparql.owlbgp.model.Annotation;
import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.ExtendedOWLObject;
import org.semanticweb.sparql.owlbgp.model.ExtendedOWLObjectVisitorEx;
import org.semanticweb.sparql.owlbgp.model.InterningManager;
import org.semanticweb.sparql.owlbgp.model.OWLAPIConverter;
import org.semanticweb.sparql.owlbgp.model.Prefixes;
import org.semanticweb.sparql.owlbgp.model.Variable;
import org.semanticweb.sparql.owlbgp.model.Variable.VarType;
import org.semanticweb.sparql.owlbgp.model.individuals.Individual;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;


public class ObjectPropertyAssertion extends AbstractAxiom implements Assertion {
    private static final long serialVersionUID = -1819707843103417338L;

    protected static InterningManager<ObjectPropertyAssertion> s_interningManager=new InterningManager<ObjectPropertyAssertion>() {
        protected boolean equal(ObjectPropertyAssertion object1,ObjectPropertyAssertion object2) {
            if (object1.m_ope!=object2.m_ope
                    ||object1.m_individual1!=object2.m_individual1
                    ||object1.m_individual2!=object2.m_individual2
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
        protected int getHashCode(ObjectPropertyAssertion object) {
            int hashCode=7*object.m_ope.hashCode()+11*object.m_individual1.hashCode()+53*object.m_individual2.hashCode();
            for (Annotation anno : object.m_annotations)
                hashCode+=anno.hashCode();
            return hashCode;
        }
    };
    
    protected final ObjectPropertyExpression m_ope;
    protected final Individual m_individual1;
    protected final Individual m_individual2;
   
    protected ObjectPropertyAssertion(ObjectPropertyExpression ope,Individual individual1,Individual individual2,Set<Annotation> annotations) {
        super(annotations);
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
        writeAnnoations(buffer, prefixes);
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
    public static ObjectPropertyAssertion create(ObjectPropertyExpression ope,Individual individual1,Individual individual2) {
        return create(ope,individual1,individual2,new HashSet<Annotation>());
    }
    public static ObjectPropertyAssertion create(ObjectPropertyExpression ope,Individual individual1,Individual individual2,Set<Annotation> annotations) {
        return s_interningManager.intern(new ObjectPropertyAssertion(ope,individual1,individual2,annotations));
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
        getAnnotationVariables(varType, variables);
        return variables;
    }
    public ExtendedOWLObject getBoundVersion(Map<Variable,Atomic> variablesToBindings) {
        return create((ObjectPropertyExpression)m_ope.getBoundVersion(variablesToBindings), (Individual)m_individual1.getBoundVersion(variablesToBindings), (Individual)m_individual2.getBoundVersion(variablesToBindings),getBoundAnnotations(variablesToBindings));
    }
    public Axiom getAxiomWithoutAnnotations() {
        return create(m_ope, m_individual1, m_individual2);
    }
}
