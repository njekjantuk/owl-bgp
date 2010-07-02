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


public class LiteralVariable extends Variable implements ILiteral {
    private static final long serialVersionUID = -2509521674677767182L;

    protected static InterningManager<LiteralVariable> s_interningManager=new InterningManager<LiteralVariable>() {
        protected boolean equal(LiteralVariable object1,LiteralVariable object2) {
            return object1.m_variable==object2.m_variable;
        }
        protected int getHashCode(LiteralVariable object) {
            return object.m_variable.hashCode();
        }
    };
    
    protected Literal m_literalBinding;
    
    protected LiteralVariable(String variable) {
        super(variable);
    }
    public String getLexicalForm() {
        if (m_literalBinding==null) return null;
        return m_literalBinding.getLexicalForm();
    }
    public String getLangTag() {
        if (m_literalBinding==null) return null;
        return m_literalBinding.getLangTag();
    }
    public Datatype getDatatype() {
        if (m_literalBinding==null) return null;
        return m_literalBinding.getDatatype();
    }
    public ILiteral getBindingAsExtendedOWLObject() {
        if (m_literalBinding==null) return null;
        return m_literalBinding;
    }
    public void setBinding(Literal binding) {
        if (binding==null) m_binding=null;
        else m_literalBinding=binding;
    }
    public void setBinding(String binding) {
        if (binding==null) m_literalBinding=null;
        else m_literalBinding=Literal.create(binding);
    }
    public String toString(Prefixes prefixes) {
        if (m_literalBinding!=null) return m_variable+"/"+m_literalBinding.toString(prefixes);
        return m_variable;
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static LiteralVariable create(String variable) {
        return s_interningManager.intern(new LiteralVariable(variable));
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
    public void applyVariableBindings(Map<Variable,ExtendedOWLObject> variablesToBindings) {
        ExtendedOWLObject binding=variablesToBindings.get(this);
        if (binding==null)
            m_literalBinding=null;
        else if (!(binding instanceof Literal))
            throw new RuntimeException("Error: Only literals can be assigned to literal variables, but literal variable "+m_variable+" was assigned the non-literal "+binding);
        else 
            m_literalBinding=(Literal)variablesToBindings;
    }
}
