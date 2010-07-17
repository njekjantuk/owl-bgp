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

public class ObjectPropertyDomain extends AbstractAxiom implements ObjectPropertyAxiom {
    private static final long serialVersionUID = 3117954908697132827L;

    protected static InterningManager<ObjectPropertyDomain> s_interningManager=new InterningManager<ObjectPropertyDomain>() {
        protected boolean equal(ObjectPropertyDomain object1,ObjectPropertyDomain object2) {
            if (object1.m_ope!=object2.m_ope
                || object1.m_classExpression!=object2.m_classExpression
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
        protected int getHashCode(ObjectPropertyDomain object) {
            int hashCode=43+11*object.m_ope.hashCode()+17*object.m_classExpression.hashCode();
            for (Annotation anno : object.m_annotations)
                hashCode+=anno.hashCode();
            return hashCode;
        }
    };
    
    protected final ObjectPropertyExpression m_ope;
    protected final ClassExpression m_classExpression;
   
    protected ObjectPropertyDomain(ObjectPropertyExpression ope,ClassExpression classExpression,Set<Annotation> annotations) {
        m_ope=ope;
        m_classExpression=classExpression;
        m_annotations=annotations;
    }
    public ObjectPropertyExpression getObjectPropertyExpression() {
        return m_ope;
    }
    public ClassExpression getDomain() {
        return m_classExpression;
    }
    public String toString(Prefixes prefixes) {
        StringBuffer buffer=new StringBuffer();
        buffer.append("ObjectPropertyDomain(");
        writeAnnoations(buffer, prefixes);
        buffer.append(m_ope.toString(prefixes));
        buffer.append(" ");
        buffer.append(m_classExpression.toString(prefixes));
        buffer.append(")");
        return buffer.toString();
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static ObjectPropertyDomain create(ObjectPropertyExpression ope,ClassExpression classExpression) {
        return ObjectPropertyDomain.create(ope,classExpression,new HashSet<Annotation>());
    }
    public static ObjectPropertyDomain create(ObjectPropertyExpression ope,ClassExpression classExpression,Set<Annotation> annotations) {
        return s_interningManager.intern(new ObjectPropertyDomain(ope,classExpression,annotations));
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
        variables.addAll(m_classExpression.getVariablesInSignature(varType));
        getAnnotationVariables(varType, variables);
        return variables;
    }
    public Set<Variable> getUnboundVariablesInSignature(VarType varType) {
        Set<Variable> unbound=new HashSet<Variable>();
        unbound.addAll(m_ope.getUnboundVariablesInSignature(varType));
        unbound.addAll(m_classExpression.getUnboundVariablesInSignature(varType));
        getUnboundAnnotationVariables(varType, unbound);
        return unbound;
    }
    public void applyBindings(Map<Variable,Atomic> variablesToBindings) {
        m_ope.applyBindings(variablesToBindings);
        m_classExpression.applyBindings(variablesToBindings);
    }
    public Axiom getAxiomWithoutAnnotations() {
        return ObjectPropertyDomain.create(m_ope, m_classExpression);
    }
}
