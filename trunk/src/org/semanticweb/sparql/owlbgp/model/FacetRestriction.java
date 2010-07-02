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


public class FacetRestriction extends AbstractExtendedOWLObject {
    private static final long serialVersionUID = -3713040265806923820L;

    protected static InterningManager<FacetRestriction> s_interningManager=new InterningManager<FacetRestriction>() {
        protected boolean equal(FacetRestriction object1,FacetRestriction object2) {
            return object1.m_facet==object2.m_facet&&object1.m_literal==object2.m_literal;
        }
        protected int getHashCode(FacetRestriction object) {
            return object.m_facet.hashCode()+object.m_literal.hashCode();
        }
    };
    
    protected final Facet m_facet;
    protected final Literal m_literal;
   
    protected FacetRestriction(Facet facet,Literal literal) {
        m_facet=facet;
        m_literal=literal;
    }
    public String toString(Prefixes prefixes) {
        return m_facet.toString(prefixes)+" "+m_literal.toString(prefixes);
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static FacetRestriction create(Facet facet,Literal literal) {
        return s_interningManager.intern(new FacetRestriction(facet,literal));
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
        return new HashSet<Variable>();
    }
    public Set<Variable> getUnboundVariablesInSignature(VarType varType) {
        return new HashSet<Variable>();
    }
}
