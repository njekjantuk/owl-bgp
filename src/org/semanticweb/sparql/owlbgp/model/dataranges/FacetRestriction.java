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
package org.semanticweb.sparql.owlbgp.model.dataranges;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.sparql.owlbgp.model.AbstractExtendedOWLObject;
import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.ExtendedOWLObject;
import org.semanticweb.sparql.owlbgp.model.ExtendedOWLObjectVisitorEx;
import org.semanticweb.sparql.owlbgp.model.InterningManager;
import org.semanticweb.sparql.owlbgp.model.OWLAPIConverter;
import org.semanticweb.sparql.owlbgp.model.Prefixes;
import org.semanticweb.sparql.owlbgp.model.Variable;
import org.semanticweb.sparql.owlbgp.model.Variable.VarType;
import org.semanticweb.sparql.owlbgp.model.literals.Literal;
import org.semanticweb.sparql.owlbgp.model.literals.TypedLiteral;


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
    protected final TypedLiteral m_literal;
   
    protected FacetRestriction(Facet facet,TypedLiteral literal) {
        m_facet=facet;
        m_literal=literal;
    }
    public Facet getFacet() {
        return m_facet;
    }
    public Literal getLiteral() {
        return m_literal;
    }
    public String toString(Prefixes prefixes) {
        return m_facet.toString(prefixes)+" "+m_literal.toString(prefixes);
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static FacetRestriction create(Facet facet,TypedLiteral literal) {
        return s_interningManager.intern(new FacetRestriction(facet,literal));
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
    public ExtendedOWLObject getBoundVersion(Map<Variable,Atomic> variablesToBindings) {
        return this;
    }
}
