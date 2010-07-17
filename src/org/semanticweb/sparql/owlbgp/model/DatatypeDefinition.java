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

public class DatatypeDefinition extends AbstractAxiom {
    private static final long serialVersionUID = -3515748752748732664L;

    protected static InterningManager<DatatypeDefinition> s_interningManager=new InterningManager<DatatypeDefinition>() {
        protected boolean equal(DatatypeDefinition object1,DatatypeDefinition object2) {
            if (object1.m_datatype!=object2.m_datatype
                    ||object1.m_dataRange!=object2.m_dataRange
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
        protected int getHashCode(DatatypeDefinition object) {
            int hashCode=27+11*object.m_datatype.hashCode()+37*object.m_dataRange.hashCode();
            for (Annotation anno : object.m_annotations)
                hashCode+=anno.hashCode();
            return hashCode;
        }
    };
    
    protected final Datatype m_datatype;
    protected final DataRange m_dataRange;
    
    protected DatatypeDefinition(Datatype datatype,DataRange dataRange,Set<Annotation> annotations) {
        m_datatype=datatype;
        m_dataRange=dataRange;
        m_annotations=annotations;
    }

    public Datatype getDatatype() {
        return m_datatype;
    }
    public DataRange getDataRange() {
        return m_dataRange;
    }
    public String toString(Prefixes prefixes) {
        StringBuffer buffer=new StringBuffer();
        buffer.append("DatatypeDefinition(");
        writeAnnoations(buffer, prefixes);
        buffer.append(m_datatype.toString(prefixes));
        buffer.append(" ");
        buffer.append(m_dataRange.toString(prefixes));
        buffer.append(")");
        return buffer.toString();
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static DatatypeDefinition create(Datatype datatype,DataRange dataRange) {
        return DatatypeDefinition .create(datatype,dataRange,new HashSet<Annotation>());
    }
    public static DatatypeDefinition create(Datatype datatype,DataRange dataRange,Set<Annotation> annotations) {
        return s_interningManager.intern(new DatatypeDefinition(datatype,dataRange,annotations));
    }
    public <O> O accept(ExtendedOWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
    protected OWLObject convertToOWLAPIObject(OWLAPIConverter converter) {
        return converter.visit(this);
    }
    public Set<Variable> getVariablesInSignature(VarType varType) {
        Set<Variable> variables=new HashSet<Variable>();
        variables.addAll(m_datatype.getVariablesInSignature(varType));
        variables.addAll(m_dataRange.getVariablesInSignature(varType));
        getAnnotationVariables(varType, variables);
        return variables;
    }
    public Set<Variable> getUnboundVariablesInSignature(VarType varType) {
        Set<Variable> unbound=new HashSet<Variable>();
        unbound.addAll(m_datatype.getUnboundVariablesInSignature(varType));
        unbound.addAll(m_dataRange.getUnboundVariablesInSignature(varType));
        getUnboundAnnotationVariables(varType, unbound);
        return unbound;
    }
    public void applyBindings(Map<Variable,Atomic> variablesToBindings) {
        m_datatype.applyBindings(variablesToBindings);
        m_dataRange.applyBindings(variablesToBindings);
    }
    public Axiom getAxiomWithoutAnnotations() {
        return DatatypeDefinition.create(m_datatype, m_dataRange);
    }
}
