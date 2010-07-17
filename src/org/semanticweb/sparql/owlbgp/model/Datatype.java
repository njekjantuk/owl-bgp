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


public class Datatype extends AbstractExtendedOWLObject implements DataRange,Atomic {
    private static final long serialVersionUID = -5589507335866233523L;

    protected static InterningManager<Datatype> s_interningManager=new InterningManager<Datatype>() {
        protected boolean equal(Datatype object1,Datatype object2) {
            return object1.m_iri==object2.m_iri;
        }
        protected int getHashCode(Datatype object) {
            return object.m_iri.hashCode();
        }
    };
    
    public enum OWL2_DATATYPES {
        LITERAL(Prefixes.s_semanticWebPrefixes.get("rdfs")+"Literal"),
        PLAIN_LITERAL(Prefixes.s_semanticWebPrefixes.get("rdf")+"PlainLiteral"),
        XML_LITERAL(Prefixes.s_semanticWebPrefixes.get("rdf")+"XMLLiteral"),
        REAL(Prefixes.s_semanticWebPrefixes.get("owl")+"real"),
        RATIONAL(Prefixes.s_semanticWebPrefixes.get("owl")+"rational"),
        STRINg(Prefixes.s_semanticWebPrefixes.get("xsd")+"string"),
        INTEGER(Prefixes.s_semanticWebPrefixes.get("xsd")+"integer"),
        LONG(Prefixes.s_semanticWebPrefixes.get("xsd")+"long"),
        INT(Prefixes.s_semanticWebPrefixes.get("xsd")+"int"),
        SHORT(Prefixes.s_semanticWebPrefixes.get("xsd")+"short"),
        BYTE(Prefixes.s_semanticWebPrefixes.get("xsd")+"byte"),
        DECIMAL(Prefixes.s_semanticWebPrefixes.get("xsd")+"decimal"),
        FLOAT(Prefixes.s_semanticWebPrefixes.get("xsd")+"float"),
        BOOLEAN(Prefixes.s_semanticWebPrefixes.get("xsd")+"boolean"),
        DOUBLE(Prefixes.s_semanticWebPrefixes.get("xsd")+"double"),
        NON_POSITIVE_INTEGER(Prefixes.s_semanticWebPrefixes.get("xsd")+"nonPositiveInteger"),
        NEGATIVE_INTEGER(Prefixes.s_semanticWebPrefixes.get("xsd")+"negativeInteger"),
        NON_NEGATIVE_INTEGER(Prefixes.s_semanticWebPrefixes.get("xsd")+"nonNegativeInteger"),
        UNSIGNED_LONG(Prefixes.s_semanticWebPrefixes.get("xsd")+"unsignedLong"),
        UNSIGNED_INT(Prefixes.s_semanticWebPrefixes.get("xsd")+"unsignedInt"),
        POSITIVE_INTEGER(Prefixes.s_semanticWebPrefixes.get("xsd")+"positiveInteger"),
        BASE64_bINARY(Prefixes.s_semanticWebPrefixes.get("xsd")+"base64Binary"),
        HEX_BINARY(Prefixes.s_semanticWebPrefixes.get("xsd")+"hexBinary"),
        ANY_URI(Prefixes.s_semanticWebPrefixes.get("xsd")+"anyURI"),
        QNAME(Prefixes.s_semanticWebPrefixes.get("xsd")+"QName"),
        NORMALIZED_STRING(Prefixes.s_semanticWebPrefixes.get("xsd")+"normalizedString"),
        TOKEN(Prefixes.s_semanticWebPrefixes.get("xsd")+"token"),
        NAME(Prefixes.s_semanticWebPrefixes.get("xsd")+"Name"),
        NCNAME(Prefixes.s_semanticWebPrefixes.get("xsd")+"NCName"),
        NMTOKEN(Prefixes.s_semanticWebPrefixes.get("xsd")+"NMToken"),
        DATETIME(Prefixes.s_semanticWebPrefixes.get("xsd")+"dateTime"),
        DATETIMESTAMP(Prefixes.s_semanticWebPrefixes.get("xsd")+"dateTimeStamp"),
        UNSIGNEDSHORT(Prefixes.s_semanticWebPrefixes.get("xsd")+"unsignedShort"),
        UNSIGNEDBYTE(Prefixes.s_semanticWebPrefixes.get("xsd")+"unsignedByte");
        
        protected final Datatype datatype; 
        
        OWL2_DATATYPES(String datatypeURI) {
            this.datatype=Datatype.create(IRI.create(datatypeURI));
        }
        public Datatype getDatatype() {
            return datatype;
        }
    }
    public boolean isOWL2Datatype() {
        for (OWL2_DATATYPES dt : OWL2_DATATYPES.values())
            if (this.m_iri==dt.getDatatype().m_iri) return true;
        return false;
    }

    protected final IRI m_iri;
   
    protected Datatype(IRI iri) {
        m_iri=iri;
    }
    public String getIRIString() {
        return m_iri.m_iri;
    }
    public String toString(Prefixes prefixes) {
        return m_iri.toString(prefixes);
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static Datatype create(String iriString) {
        return create(IRI.create(iriString));
    }
    public static Datatype create(IRI iri) {
        return s_interningManager.intern(new Datatype(iri));
    }
    public Identifier getIdentifier() {
        return m_iri;
    }
    public <O> O accept(ExtendedOWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
    protected OWLObject convertToOWLAPIObject(OWLAPIConverter converter) {
        return converter.visit(this);
    }
    public Set<Variable> getVariablesInSignature(VarType varType) {
        return new HashSet<Variable>();
    }
    public Set<Variable> getUnboundVariablesInSignature(VarType varType) {
        return new HashSet<Variable>();
    }
}
