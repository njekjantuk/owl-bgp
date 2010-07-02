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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.sparql.owlbgp.model.Variable.VarType;

public class ObjectIntersectionOf extends AbstractExtendedOWLObject implements ClassExpression {
    private static final long serialVersionUID = -3579978710820477558L;

    protected static InterningManager<ObjectIntersectionOf> s_interningManager=new InterningManager<ObjectIntersectionOf>() {
        protected boolean equal(ObjectIntersectionOf intersection1,ObjectIntersectionOf intersection2) {
            if (intersection1.m_classExpressions.size()!=intersection2.m_classExpressions.size())
                return false;
            for (ClassExpression conjunct : intersection1.m_classExpressions) {
                if (!contains(conjunct, intersection2.m_classExpressions))
                    return false;
            } 
            return true;
        }
        protected boolean contains(ClassExpression classExpression,Set<ClassExpression> classExpressions) {
            for (ClassExpression conjunct: classExpressions)
                if (conjunct==classExpression)
                    return true;
            return false;
        }
        protected int getHashCode(ObjectIntersectionOf intersection) {
            int hashCode=0;
            for (ClassExpression conjunct : intersection.m_classExpressions)
                hashCode+=conjunct.hashCode();
            return hashCode;
        }
    };
    
    protected final Set<ClassExpression> m_classExpressions;
    
    protected ObjectIntersectionOf(Set<ClassExpression> classExpressions) {
        m_classExpressions=classExpressions;
    }
    public  Set<ClassExpression> getClassExpressions() {
        return m_classExpressions;
    }
    public String toString(Prefixes prefixes) {
        StringBuffer buffer=new StringBuffer();
        buffer.append("ObjectIntersectionOf(");
        boolean notFirst=false;
        for (ClassExpression conjunct : m_classExpressions) {
            if (notFirst)
                buffer.append(' ');
            else 
                notFirst=true;
            buffer.append(conjunct.toString(prefixes));
        }
        buffer.append(")");
        return buffer.toString();
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static ObjectIntersectionOf create(Set<ClassExpression> classExpressions) {
        return s_interningManager.intern(new ObjectIntersectionOf(classExpressions));
    }
    public static ObjectIntersectionOf create(ClassExpression... classExpressions) {
        return s_interningManager.intern(new ObjectIntersectionOf(new HashSet<ClassExpression>(Arrays.asList(classExpressions))));
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
        for (ClassExpression classExpression : m_classExpressions) {
            variables.addAll(classExpression.getVariablesInSignature(varType));
        }
        return variables;
    }
    public Set<Variable> getUnboundVariablesInSignature(VarType varType) {
        Set<Variable> unbound=new HashSet<Variable>();
        for (ClassExpression ce : m_classExpressions) 
            unbound.addAll(ce.getUnboundVariablesInSignature(varType));
        return unbound;
    }
    public void applyBindings(Map<String,String> variablesToBindings) {
        for (ClassExpression ce : m_classExpressions)
            ce.applyBindings(variablesToBindings);
    }
    public void applyVariableBindings(Map<Variable,ExtendedOWLObject> variablesToBindings) {
        for (ClassExpression ce : m_classExpressions)
            ce.applyVariableBindings(variablesToBindings);
    }
}