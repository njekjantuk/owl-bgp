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


public class Facet {
    private static final long serialVersionUID = -5165781649617062849L;

    protected static InterningManager<Facet> s_interningManager=new InterningManager<Facet>() {
        protected boolean equal(Facet object1,Facet object2) {
            return object1.m_iri==object2.m_iri;
        }
        protected int getHashCode(Facet object) {
            return object.m_iri.hashCode();
        }
    };
    
    protected final String m_iri;
   
    protected Facet(String iri) {
        m_iri=iri.intern();
    }
    public String getIRIString() {
        return m_iri;
    }
    public String toString(Prefixes prefixes) {
        return prefixes.abbreviateIRI(m_iri);
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static Facet create(String iri) {
        return s_interningManager.intern(new Facet(iri));
    }
    public String getIdentifier() {
        return m_iri;
    }
}
