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

public class FunctionalDataProperty extends AbstractAxiom implements DataPropertyAxiom {
    private static final long serialVersionUID = -1638368948955304491L;

    protected static InterningManager<FunctionalDataProperty> s_interningManager=new InterningManager<FunctionalDataProperty>() {
        protected boolean equal(FunctionalDataProperty object1,FunctionalDataProperty object2) {
            if (object1.m_dpe==object2.m_dpe
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
        protected int getHashCode(FunctionalDataProperty object) {
            int hashCode=17+13*object.m_dpe.hashCode();
            for (Annotation anno : object.m_annotations)
                hashCode+=anno.hashCode();
            return hashCode;
        }
    };
    
    protected final DataPropertyExpression m_dpe;
   
    protected FunctionalDataProperty(DataPropertyExpression dataPropertyExpression,Set<Annotation> annotations) {
        m_dpe=dataPropertyExpression;
        m_annotations=annotations;
    }
    public DataPropertyExpression getDataPropertyExpression() {
        return m_dpe;
    }
    public String toString(Prefixes prefixes) {
        StringBuffer buffer=new StringBuffer();
        buffer.append("FunctionalDataProperty(");
        writeAnnoations(buffer, prefixes);
        buffer.append(m_dpe.toString(prefixes));
        buffer.append(")");
        return buffer.toString();
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static FunctionalDataProperty create(DataPropertyExpression dataPropertyExpression) {
        return FunctionalDataProperty.create(dataPropertyExpression,new HashSet<Annotation>());
    }
    public static FunctionalDataProperty create(DataPropertyExpression dataPropertyExpression,Set<Annotation> annotations) {
        return s_interningManager.intern(new FunctionalDataProperty(dataPropertyExpression,annotations));
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
        getAnnotationVariables(varType, variables);
        return variables;
    }
    public Set<Variable> getUnboundVariablesInSignature(VarType varType) {
        Set<Variable> unbound=new HashSet<Variable>();
        unbound.addAll(m_dpe.getUnboundVariablesInSignature(varType));
        getUnboundAnnotationVariables(varType, unbound);
        return unbound;
    }
    public void applyBindings(Map<Variable,Atomic> variablesToBindings) {
        m_dpe.applyBindings(variablesToBindings);
    }
    public Axiom getAxiomWithoutAnnotations() {
        return FunctionalDataProperty.create(m_dpe);
    }
}
