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

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


public class Facet implements Serializable {
    private static final long serialVersionUID = -5165781649617062849L;
    
    public static Set<Facet> OWL_FACETS=new HashSet<Facet>();
    static {
        OWL_FACETS.add(Facet.create(IRI.create(Prefixes.s_semanticWebPrefixes.get("xsd")+"minInclusive")));
        OWL_FACETS.add(Facet.create(IRI.create(Prefixes.s_semanticWebPrefixes.get("xsd")+"maxInclusive")));
        OWL_FACETS.add(Facet.create(IRI.create(Prefixes.s_semanticWebPrefixes.get("xsd")+"minExclusive")));
        OWL_FACETS.add(Facet.create(IRI.create(Prefixes.s_semanticWebPrefixes.get("xsd")+"maxExclusive")));
        OWL_FACETS.add(Facet.create(IRI.create(Prefixes.s_semanticWebPrefixes.get("xsd")+"length")));
        OWL_FACETS.add(Facet.create(IRI.create(Prefixes.s_semanticWebPrefixes.get("xsd")+"minLength")));
        OWL_FACETS.add(Facet.create(IRI.create(Prefixes.s_semanticWebPrefixes.get("xsd")+"maxLength")));
        OWL_FACETS.add(Facet.create(IRI.create(Prefixes.s_semanticWebPrefixes.get("xsd")+"pattern")));
        OWL_FACETS.add(Facet.create(IRI.create(Prefixes.s_semanticWebPrefixes.get("rdf")+"langRange")));
    }
    protected static InterningManager<Facet> s_interningManager=new InterningManager<Facet>() {
        protected boolean equal(Facet object1,Facet object2) {
            return object1.m_iri==object2.m_iri;
        }
        protected int getHashCode(Facet object) {
            return object.m_iri.hashCode();
        }
    };
    
    protected final IRI m_iri;
   
    protected Facet(IRI iri) {
        m_iri=iri;
    }
    public String getIRIString() {
        return m_iri.toString();
    }
    public String getIRI() {
        return m_iri.toString();
    }
    public String toString(Prefixes prefixes) {
        return m_iri.toString(prefixes);
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static Facet create(IRI iri) {
        return s_interningManager.intern(new Facet(iri));
    }
    public Identifier getIdentifier() {
        return m_iri;
    }
}
