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


public class LiteralVariable extends Variable implements ILiteral {
    private static final long serialVersionUID = -2509521674677767182L;
    
    protected ILiteral m_literalBinding;
    
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
    public void setBinding(ILiteral binding) {
        m_literalBinding=binding;
    }
    public void setBinding(String binding) {
        m_literalBinding=Literal.create(binding);
    }
    public String toString(Prefixes prefixes) {
        if (m_literalBinding!=null) return m_variable+"/"+m_literalBinding.toString(prefixes);
        return m_variable;
    }
    
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    protected static InterningManager<LiteralVariable> s_interningManager=new InterningManager<LiteralVariable>() {
        protected boolean equal(LiteralVariable object1,LiteralVariable object2) {
            return object1.m_variable.equals(object2.m_variable);
        }
        protected int getHashCode(LiteralVariable object) {
            return object.m_variable.hashCode();
        }
    };
    public static LiteralVariable create(String variable) {
        return s_interningManager.intern(new LiteralVariable(variable));
    }
    public <O> O accept(ExtendedOWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
    public Set<Variable> getVariablesInSignature() {
        Set<Variable> variables=new HashSet<Variable>();
        variables.add(this);
        return variables;
    }
}
