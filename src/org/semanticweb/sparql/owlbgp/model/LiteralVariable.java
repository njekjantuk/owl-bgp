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


public class LiteralVariable extends Variable implements ILiteral {
    private static final long serialVersionUID = -2509521674677767182L;

    protected static InterningManager<LiteralVariable> s_interningManager=new InterningManager<LiteralVariable>() {
        protected boolean equal(LiteralVariable object1,LiteralVariable object2) {
            return object1.m_variable==object2.m_variable&&object1.m_binding==object2.m_binding;
        }
        protected int getHashCode(LiteralVariable object) {
            int hashCode=23;
            hashCode+=object.m_variable.hashCode();
            if (object.m_binding!=null) hashCode+=object.m_binding.hashCode();
            return hashCode;
        }
    };
    
    protected LiteralVariable(String variable, Atomic binding) {
        super(variable,binding);
    }
    public String getLexicalForm() {
        if (m_binding==null) return null;
        return ((ILiteral)m_binding).getLexicalForm();
    }
    public String getLangTag() {
        if (m_binding==null) return null;
        return ((ILiteral)m_binding).getLangTag();
    }
    public Datatype getDatatype() {
        if (m_binding==null) return null;
        return ((ILiteral)m_binding).getDatatype();
    }
    public ILiteral getBindingAsExtendedOWLObject() {
        return (ILiteral)m_binding;
    }
    public void setBinding(Atomic binding) {
        if (binding==null) m_binding=null;
        if (binding instanceof ILiteral) m_binding=binding;
        throw new RuntimeException("Error: Only literals can be assigned to literal variables, but literal variable "+m_variable+" was assigned the non-literal "+binding);
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static LiteralVariable create(String variable) {
        return LiteralVariable.create(variable,null);
    }
    public static LiteralVariable create(String variable,Atomic binding) {
        return s_interningManager.intern(new LiteralVariable(variable,binding));
    }
    public <O> O accept(ExtendedOWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
    protected OWLObject convertToOWLAPIObject(OWLAPIConverter converter) {
        return converter.visit(this);
    }
    public Set<Variable> getVariablesInSignature(VarType varType) {
        Set<Variable> variables=new HashSet<Variable>();
        if (varType==null||varType==VarType.INDIVIDUAL) variables.add(this);
        return variables;
    }
    public Set<Variable> getUnboundVariablesInSignature(VarType varType) {
        Set<Variable> variables=new HashSet<Variable>();
        if (m_binding==null&&(varType==null||varType==VarType.LITERAL)) variables.add(this);
        return variables;
    }
//    public void applyVariableBindings(Map<Variable,ExtendedOWLObject> variablesToBindings) {
//        ExtendedOWLObject binding=variablesToBindings.get(this);
//        if (binding==null)
//            m_literalBinding=null;
//        else if (!(binding instanceof Literal))
//            throw new RuntimeException("Error: Only literals can be assigned to literal variables, but literal variable "+m_variable+" was assigned the non-literal "+binding);
//        else 
//            m_literalBinding=(Literal)variablesToBindings;
//    }
}
