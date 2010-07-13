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

public class AnnotationValue extends AbstractExtendedOWLObject {
    private static final long serialVersionUID = -7278978681384296549L;

    protected static InterningManager<AnnotationValue> s_interningManager=new InterningManager<AnnotationValue>() {
        protected boolean equal(AnnotationValue object1,AnnotationValue object2) {
            return object1.m_value==object2.m_value;
        }
        protected int getHashCode(AnnotationValue object) {
            return 113+13*object.m_value.hashCode();
        }
    };
    
    protected final Object m_value;
   
    protected AnnotationValue(Object annotationValue) {
        m_value=annotationValue;
    }
    public Object getAnnotationValue() {
        return m_value;
    }
    public String toString(Prefixes prefixes) {
        StringBuffer buffer=new StringBuffer();
        if (m_value instanceof Individual)
            buffer.append(((Individual)m_value).toString(prefixes));
        else if (m_value instanceof ILiteral)
            buffer.append(((ILiteral)m_value).toString(prefixes));
        else buffer.append(prefixes.abbreviateIRI(m_value.toString()));
        return buffer.toString();
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static AnnotationValue create(Object annotationValue) {
        return s_interningManager.intern(new AnnotationValue(annotationValue));
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
        if (m_value instanceof Variable) variables.add((Variable)m_value);
        return variables;
    }
    public Set<Variable> getUnboundVariablesInSignature(VarType varType) {
        Set<Variable> unbound=new HashSet<Variable>();
        if (m_value instanceof Variable) unbound.add((Variable)m_value);
        return unbound;
    }
    public void applyBindings(Map<String,String> variablesToBindings) {
    }
    public void applyVariableBindings(Map<Variable,ExtendedOWLObject> variablesToBindings) {
    }
}
