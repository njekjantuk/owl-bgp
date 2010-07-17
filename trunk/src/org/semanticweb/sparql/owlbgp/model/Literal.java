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
import org.semanticweb.sparql.owlbgp.model.Variable.VarType;


public class Literal extends AbstractExtendedOWLObject implements ILiteral {
    private static final long serialVersionUID = -8503015611577734737L;

    protected static InterningManager<Literal> s_interningManager=new InterningManager<Literal>() {
        protected boolean equal(Literal object1,Literal object2) {
            return object1.m_lexicalForm==object2.m_lexicalForm;
        }
        protected int getHashCode(Literal object) {
            return object.m_lexicalForm.hashCode();
        }
    };
    
    protected final String m_lexicalForm;
    protected final String m_langTag;
    protected final Datatype m_dataDatatype;
    
    protected Literal(String lexicalForm,String langTag,Datatype datatype) {
        m_lexicalForm=lexicalForm.intern();
        m_langTag=langTag.intern();
        m_dataDatatype=datatype;
    }
    public String getLexicalForm() {
        return m_lexicalForm;
    }
    public String getLangTag() {
        return m_langTag;
    }
    public Datatype getDatatype() {
        return m_dataDatatype;
    }
    public String toString(Prefixes prefixes) {
        StringBuffer buffer=new StringBuffer();
        buffer.append("''");
        buffer.append(m_lexicalForm);
        if (m_langTag!=null) {
            buffer.append("@");
            buffer.append(m_langTag);
        }
        buffer.append("''");
        buffer.append("^^");
        if (m_dataDatatype==null) buffer.append("http://www.w3.org/1999/02/22-rdf-syntax-ns#PlainLiteral");
        else buffer.append(m_dataDatatype.toString(prefixes));
        return buffer.toString();
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static Literal create(String lexicalForm,String langTag,Datatype datatype) {
        return s_interningManager.intern(new Literal(lexicalForm,langTag,datatype));
    }
    public static Literal create(String literal) {
        String lexicalForm=null;
        String langTag=null;
        Datatype datatype=null;
        String noDatatype=null;
        int lastCircCircIndex=literal.lastIndexOf("^^");
        if (lastCircCircIndex>=0) {
            datatype=Datatype.create(IRI.create(literal.substring(lastCircCircIndex)));
            noDatatype=literal.substring(1,lastCircCircIndex-3);
        } else {
            datatype=Datatype.OWL2_DATATYPES.PLAIN_LITERAL.datatype;
            noDatatype=literal.substring(1,literal.length()-1);
        }
        int lastAtIndex=noDatatype.lastIndexOf("@");
        if (lastAtIndex>=0) {
            lexicalForm=noDatatype.substring(0,lastAtIndex-1);
            langTag=noDatatype.substring(lastAtIndex+1);
        } else {
            lexicalForm=noDatatype;
        }
        return s_interningManager.intern(new Literal(lexicalForm,langTag,datatype));
    }
    public <O> O accept(ExtendedOWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
    protected OWLObject convertToOWLAPIObject(OWLAPIConverter converter) {
        return converter.visit(this);
    }
    public Set<Variable> getVariablesInSignature() {
        return getVariablesInSignature(null);
    }
    public Set<Variable> getVariablesInSignature(VarType varType) {
        return new HashSet<Variable>();
    }
    public Set<Variable> getUnboundVariablesInSignature() {
        return getUnboundVariablesInSignature(null);
    }
    public Set<Variable> getUnboundVariablesInSignature(VarType varType) {
        return new HashSet<Variable>();
    }
}
