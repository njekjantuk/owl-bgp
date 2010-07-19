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
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;


public class SubObjectPropertyOf extends AbstractAxiom implements ObjectPropertyAxiom {
    private static final long serialVersionUID = -4739651270386976693L;

    protected static InterningManager<SubObjectPropertyOf> s_interningManager=new InterningManager<SubObjectPropertyOf>() {
        protected boolean equal(SubObjectPropertyOf object1,SubObjectPropertyOf object2) {
            if (object1.m_subope!=object2.m_subope
                    ||object1.m_superope!=object2.m_superope
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
        protected int getHashCode(SubObjectPropertyOf object) {
            int hashCode=object.m_subope.hashCode()+11*object.m_superope.hashCode();
            for (Annotation anno : object.m_annotations)
                hashCode+=anno.hashCode();
            return hashCode;
        }
    };
    
    protected final ObjectPropertyExpression m_subope;
    protected final ObjectPropertyExpression m_superope;
    
    protected SubObjectPropertyOf(ObjectPropertyExpression subObjectPropertyExpression,ObjectPropertyExpression superObjectPropertyExpression,Set<Annotation> annotations) {
        super(annotations);
        m_subope=subObjectPropertyExpression;
        m_superope=superObjectPropertyExpression;
    }
    public ObjectPropertyExpression getSubObjectPropertyExpression() {
        return m_subope;
    }
    public ObjectPropertyExpression getSuperObjectPropertyExpression() {
        return m_superope;
    }
    public String toString(Prefixes prefixes) {
        StringBuffer buffer=new StringBuffer();
        buffer.append("SubObjectPropertyOf(");
        writeAnnoations(buffer, prefixes);
        buffer.append(m_subope.toString(prefixes));
        buffer.append(" ");
        buffer.append(m_superope.toString(prefixes));
        buffer.append(")");
        return buffer.toString();
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static SubObjectPropertyOf create(ObjectPropertyExpression subObjectPropertyExpression, ObjectPropertyExpression superObjectPropertyExpression) {
        return create(subObjectPropertyExpression,superObjectPropertyExpression,new HashSet<Annotation>());
    }
    public static SubObjectPropertyOf create(ObjectPropertyExpression subObjectPropertyExpression, ObjectPropertyExpression superObjectPropertyExpression,Set<Annotation> annotations) {
        return s_interningManager.intern(new SubObjectPropertyOf(subObjectPropertyExpression,superObjectPropertyExpression,annotations));
    }
    public <O> O accept(ExtendedOWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
    protected OWLObject convertToOWLAPIObject(OWLAPIConverter converter) {
        return converter.visit(this);
    }
    public Set<Variable> getVariablesInSignature(VarType varType) {
        Set<Variable> variables=new HashSet<Variable>();
        variables.addAll(m_subope.getVariablesInSignature(varType));
        variables.addAll(m_superope.getVariablesInSignature(varType));
        getAnnotationVariables(varType, variables);
        return variables;
    }
    public ExtendedOWLObject getBoundVersion(Map<Variable,Atomic> variablesToBindings) {
        return create((ObjectPropertyExpression)m_subope.getBoundVersion(variablesToBindings),(ObjectPropertyExpression)m_superope.getBoundVersion(variablesToBindings),getBoundAnnotations(variablesToBindings));
    }
    public Axiom getAxiomWithoutAnnotations() {
        return create(m_subope, m_superope);
    }
}
