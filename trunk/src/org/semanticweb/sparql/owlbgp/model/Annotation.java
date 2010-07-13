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

public class Annotation extends AbstractExtendedOWLObject implements ClassExpression {
    private static final long serialVersionUID = -4586686325214553112L;

    protected static InterningManager<Annotation> s_interningManager=new InterningManager<Annotation>() {
        protected boolean equal(Annotation object1,Annotation object2) {
            return object1.m_ap==object2.m_ap && object1.m_value==object2.m_value;
        }
        protected int getHashCode(Annotation object) {
            return 7*object.m_ap.hashCode()+13*object.m_value.hashCode();
        }
    };
    
    protected final AnnotationProperty m_ap;
    protected final AnnotationValue m_value;
    protected final Set<Annotation> m_annotations;
    
    protected Annotation(AnnotationProperty annotationProperty,AnnotationValue annotationValue,Set<Annotation> annotations) {
        m_ap=annotationProperty;
        m_value=annotationValue;
        m_annotations=annotations;
    }
    public AnnotationProperty getAnnotationProperty() {
        return m_ap;
    }
    public AnnotationValue getAnnotationValue() {
        return m_value;
    }
    public Set<Annotation> getAnnotations() {
        return m_annotations;
    }
    public String toString(Prefixes prefixes) {
        StringBuffer sb=new StringBuffer();
        sb.append("Annotation(");
        boolean notFirst=false;
        for (Annotation anno : m_annotations) {
            if (notFirst) sb.append(" ");
            else notFirst=true;
            sb.append(anno.toString(prefixes));
        }
        sb.append(m_ap.toString(prefixes));
        sb.append(" ");
        sb.append(m_value.toString(prefixes));
        sb.append(")");
        return sb.toString();
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static Annotation create(AnnotationProperty annotationProperty,AnnotationValue annotationValue) {
        return s_interningManager.intern(new Annotation(annotationProperty,annotationValue,new HashSet<Annotation>()));
    }
    public static Annotation create(AnnotationProperty annotationProperty,AnnotationValue annotationValue,Set<Annotation> annotations) {
        return s_interningManager.intern(new Annotation(annotationProperty,annotationValue,annotations));
    }
    public String getIdentifier() {
        return null;
    }
    public <O> O accept(ExtendedOWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
    protected OWLObject convertToOWLAPIObject(OWLAPIConverter converter) {
        return converter.visit(this);
    }
    public Set<Variable> getVariablesInSignature(VarType varType) {
        Set<Variable> variables=new HashSet<Variable>();
        variables.addAll(m_ap.getVariablesInSignature(varType));
        variables.addAll(m_value.getVariablesInSignature(varType));
        return variables;
    }
    public Set<Variable> getUnboundVariablesInSignature(VarType varType) {
        Set<Variable> unbound=new HashSet<Variable>();
        unbound.addAll(m_ap.getUnboundVariablesInSignature(varType));
        unbound.addAll(m_value.getUnboundVariablesInSignature(varType));
        return unbound;
    }
    public void applyBindings(Map<String,String> variablesToBindings) {
        m_ap.applyBindings(variablesToBindings);
        m_value.applyBindings(variablesToBindings);
    }
    public void applyVariableBindings(Map<Variable,ExtendedOWLObject> variablesToBindings) {
        m_ap.applyVariableBindings(variablesToBindings);
        m_value.applyVariableBindings(variablesToBindings);
    }
}
