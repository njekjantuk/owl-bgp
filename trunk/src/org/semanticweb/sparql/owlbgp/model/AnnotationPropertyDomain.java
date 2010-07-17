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

public class AnnotationPropertyDomain extends AbstractAxiom implements ObjectPropertyAxiom {
    private static final long serialVersionUID = 3117954908697132827L;

    protected static InterningManager<AnnotationPropertyDomain> s_interningManager=new InterningManager<AnnotationPropertyDomain>() {
        protected boolean equal(AnnotationPropertyDomain object1,AnnotationPropertyDomain object2) {
            if (object1.m_annotationPropertyExpression!=object2.m_annotationPropertyExpression
                || object1.m_domain!=object2.m_domain
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
        protected int getHashCode(AnnotationPropertyDomain object) {
            int hashCode=43+11*object.m_annotationPropertyExpression.hashCode()+17*object.m_domain.hashCode();
            for (Annotation anno : object.m_annotations)
                hashCode+=anno.hashCode();
            return hashCode;
        }
    };
    
    protected final AnnotationPropertyExpression m_annotationPropertyExpression;
    protected final IRI m_domain;
   
    protected AnnotationPropertyDomain(AnnotationPropertyExpression ope,IRI classExpression,Set<Annotation> annotations) {
        m_annotationPropertyExpression=ope;
        m_domain=classExpression;
        m_annotations=annotations;
    }
    public AnnotationPropertyExpression getAnnotationPropertyExpression() {
        return m_annotationPropertyExpression;
    }
    public IRI getDomain() {
        return m_domain;
    }
    public String toString(Prefixes prefixes) {
        StringBuffer buffer=new StringBuffer();
        buffer.append("AnnotationPropertyDomain(");
        writeAnnoations(buffer, prefixes);
        buffer.append(m_annotationPropertyExpression.toString(prefixes));
        buffer.append(" ");
        buffer.append(m_domain.toString(prefixes));
        buffer.append(")");
        return buffer.toString();
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static AnnotationPropertyDomain create(AnnotationPropertyExpression annotationPropertyExpression,IRI iri) {
        return AnnotationPropertyDomain.create(annotationPropertyExpression,iri,new HashSet<Annotation>());
    }
    public static AnnotationPropertyDomain create(AnnotationPropertyExpression annotationPropertyExpression,IRI iri,Set<Annotation> annotations) {
        return s_interningManager.intern(new AnnotationPropertyDomain(annotationPropertyExpression,iri,annotations));
    }
    public <O> O accept(ExtendedOWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
    protected OWLObject convertToOWLAPIObject(OWLAPIConverter converter) {
        return converter.visit(this);
    }
    public Set<Variable> getVariablesInSignature(VarType varType) {
        Set<Variable> variables=new HashSet<Variable>();
        variables.addAll(m_annotationPropertyExpression.getVariablesInSignature(varType));
        variables.addAll(m_domain.getVariablesInSignature(varType));
        getAnnotationVariables(varType, variables);
        return variables;
    }
    public Set<Variable> getUnboundVariablesInSignature(VarType varType) {
        Set<Variable> unbound=new HashSet<Variable>();
        unbound.addAll(m_annotationPropertyExpression.getUnboundVariablesInSignature(varType));
        unbound.addAll(m_domain.getUnboundVariablesInSignature(varType));
        getUnboundAnnotationVariables(varType, unbound);
        return unbound;
    }
    public void applyBindings(Map<Variable,Atomic> variablesToBindings) {
        m_annotationPropertyExpression.applyBindings(variablesToBindings);
        m_domain.applyBindings(variablesToBindings);
    }
    public Axiom getAxiomWithoutAnnotations() {
        return AnnotationPropertyDomain.create(m_annotationPropertyExpression, m_domain);
    }
}
