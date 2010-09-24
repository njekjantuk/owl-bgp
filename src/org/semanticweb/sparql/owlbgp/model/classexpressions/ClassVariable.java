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
package org.semanticweb.sparql.owlbgp.model.classexpressions;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.ExtendedOWLObject;
import org.semanticweb.sparql.owlbgp.model.ExtendedOWLObjectVisitor;
import org.semanticweb.sparql.owlbgp.model.ExtendedOWLObjectVisitorEx;
import org.semanticweb.sparql.owlbgp.model.InterningManager;
import org.semanticweb.sparql.owlbgp.model.OWLAPIConverter;
import org.semanticweb.sparql.owlbgp.model.Variable;

public class ClassVariable extends Variable implements ClassExpression {
    private static final long serialVersionUID = -4710499264999865534L;

    protected static InterningManager<ClassVariable> s_interningManager=new InterningManager<ClassVariable>() {
        protected boolean equal(ClassVariable object1,ClassVariable object2) {
            return object1.m_variable==object2.m_variable;
        }
        protected int getHashCode(ClassVariable object) {
            int hashCode=47;
            hashCode+=object.m_variable.hashCode();
            return hashCode;
        }
    };
    
    protected ClassVariable(String variable) {
        super(variable);
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static ClassVariable create(String variable) {
        return s_interningManager.intern(new ClassVariable(variable));
    }
    @Override
    public <O> O accept(ExtendedOWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
    public void accept(ExtendedOWLObjectVisitor visitor) {
        visitor.visit(this);
    }
    @Override
    protected OWLObject convertToOWLAPIObject(OWLAPIConverter converter) {
        return converter.visit(this);
    }
    @Override
    public Set<Variable> getVariablesInSignature(VarType varType) {
        Set<Variable> variables=new HashSet<Variable>();
        if (varType==null||varType==VarType.CLASS) variables.add(this);
        return variables;
    }
    @Override
    public ExtendedOWLObject getBoundVersion(Atomic binding) {
        if (binding instanceof Clazz || binding==null) return binding;
        else throw new RuntimeException("Error: Only classes can be assigned to class variables, but class variable "+m_variable+" was assigned the non-class "+binding);
    }
}
