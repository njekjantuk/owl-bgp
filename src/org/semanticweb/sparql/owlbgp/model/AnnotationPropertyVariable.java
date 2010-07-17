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
import java.util.Set;

import org.semanticweb.owlapi.model.OWLObject;

public class AnnotationPropertyVariable extends Variable implements AnnotationPropertyExpression,Atomic {
    private static final long serialVersionUID = -971279445618480330L;
    
    protected static InterningManager<AnnotationPropertyVariable> s_interningManager=new InterningManager<AnnotationPropertyVariable>() {
        protected boolean equal(AnnotationPropertyVariable object1,AnnotationPropertyVariable object2) {
            return object1.m_variable==object2.m_variable&&object1.m_binding==object2.m_binding;
        }
        protected int getHashCode(AnnotationPropertyVariable object) {
            int hashCode=71;
            hashCode+=object.m_variable.hashCode();
            if (object.m_binding!=null) hashCode+=object.m_binding.hashCode();
            return hashCode;
        }
    };
    protected AnnotationPropertyVariable(String variable,Atomic binding) {
        super(variable,binding);
    }
    public AnnotationProperty getBindingAsExtendedOWLObject() {
        if (m_binding==null) return null;
        return (AnnotationProperty)m_binding;
    }
    public void setBinding(Atomic binding) {
        if (binding==null) m_binding=null;
        else if (binding instanceof AnnotationProperty) m_binding=binding;
        else throw new RuntimeException("Error: Only annotation properties can be assigned to annotation property variables, but annotation property variable "+m_variable+" was assigned the non-annotation property "+binding);
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static AnnotationPropertyVariable create(String variable) {
        return AnnotationPropertyVariable.create(variable,null);
    }
    public static AnnotationPropertyVariable create(String variable,Atomic binding) {
        return s_interningManager.intern(new AnnotationPropertyVariable(variable,binding));
    }
    public <O> O accept(ExtendedOWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
    protected OWLObject convertToOWLAPIObject(OWLAPIConverter converter) {
        return converter.visit(this);
    }
    public Set<Variable> getVariablesInSignature(VarType varType) {
        Set<Variable> variables=new HashSet<Variable>();
        if (varType==null||varType==VarType.ANNOTATION_PROPERTY) variables.add(this);
        return variables;
    }
    public Set<Variable> getUnboundVariablesInSignature(VarType varType) {
        Set<Variable> variables=new HashSet<Variable>();
        if (m_binding==null&&(varType==null||varType==VarType.ANNOTATION_PROPERTY)) variables.add(this);
        return variables;
    }
}
