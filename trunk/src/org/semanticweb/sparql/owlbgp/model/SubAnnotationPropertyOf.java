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

public class SubAnnotationPropertyOf extends AbstractAxiom {
    private static final long serialVersionUID = 8392842433326185976L;

    protected static InterningManager<SubAnnotationPropertyOf> s_interningManager=new InterningManager<SubAnnotationPropertyOf>() {
        protected boolean equal(SubAnnotationPropertyOf object1,SubAnnotationPropertyOf object2) {
            if (object1.m_subap!=object2.m_subap
                    ||object1.m_superap!=object2.m_superap
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
        protected int getHashCode(SubAnnotationPropertyOf object) {
            int hashCode=object.m_subap.hashCode()+11*object.m_superap.hashCode();
            for (Annotation anno : object.m_annotations)
                hashCode+=anno.hashCode();
            return hashCode;
        }
    };
    
    protected final AnnotationProperty m_subap;
    protected final AnnotationProperty m_superap;
    
    protected SubAnnotationPropertyOf(AnnotationProperty subObjectPropertyExpression,AnnotationProperty superObjectPropertyExpression,Set<Annotation> annotations) {
        m_subap=subObjectPropertyExpression;
        m_superap=superObjectPropertyExpression;
        m_annotations=annotations;
    }
    public AnnotationProperty getSubAnnotationPropertyExpression() {
        return m_subap;
    }
    public AnnotationProperty getSuperObjectPropertyExpression() {
        return m_superap;
    }
    public String toString(Prefixes prefixes) {
        StringBuffer buffer=new StringBuffer();
        buffer.append("SubAnnotationPropertyOf(");
        writeAnnoations(buffer, prefixes);
        buffer.append(m_subap.toString(prefixes));
        buffer.append(" ");
        buffer.append(m_superap.toString(prefixes));
        buffer.append(")");
        return buffer.toString();
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static SubAnnotationPropertyOf create(AnnotationProperty subAnnotationProperty, AnnotationProperty superAnnotationProperty) {
        return SubAnnotationPropertyOf.create(subAnnotationProperty,superAnnotationProperty,new HashSet<Annotation>());
    }
    public static SubAnnotationPropertyOf create(AnnotationProperty subAnnotationProperty, AnnotationProperty superAnnotationProperty,Set<Annotation> annotations) {
        return s_interningManager.intern(new SubAnnotationPropertyOf(subAnnotationProperty,superAnnotationProperty,annotations));
    }
    public <O> O accept(ExtendedOWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
    protected OWLObject convertToOWLAPIObject(OWLAPIConverter converter) {
        return converter.visit(this);
    }
    public Set<Variable> getVariablesInSignature(VarType varType) {
        Set<Variable> variables=new HashSet<Variable>();
        variables.addAll(m_subap.getVariablesInSignature(varType));
        variables.addAll(m_superap.getVariablesInSignature(varType));
        getAnnotationVariables(varType, variables);
        return variables;
    }
    public Set<Variable> getUnboundVariablesInSignature(VarType varType) {
        Set<Variable> unbound=new HashSet<Variable>();
        unbound.addAll(m_subap.getUnboundVariablesInSignature(varType));
        unbound.addAll(m_superap.getUnboundVariablesInSignature(varType));
        getUnboundAnnotationVariables(varType, unbound);
        return unbound;
    }
    public void applyBindings(Map<Variable,Atomic> variablesToBindings) {
        m_subap.applyBindings(variablesToBindings);
        m_superap.applyBindings(variablesToBindings);
    }
    public Axiom getAxiomWithoutAnnotations() {
        return SubAnnotationPropertyOf.create(m_subap, m_superap);
    }
}
