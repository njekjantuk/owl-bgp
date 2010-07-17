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


public class NamedIndividual extends AbstractExtendedOWLObject implements Individual, Atomic {
    private static final long serialVersionUID = -8797258383209941720L;

    protected static InterningManager<NamedIndividual> s_interningManager=new InterningManager<NamedIndividual>() {
        protected boolean equal(NamedIndividual object1,NamedIndividual object2) {
            return object1.m_iri==object2.m_iri;
        }
        protected int getHashCode(NamedIndividual object) {
            return object.m_iri.hashCode();
        }
    };
    
    protected final IRI m_iri;
   
    protected NamedIndividual(IRI iri) {
        m_iri=iri;
    }
    public String getIRIString() {
        return m_iri.toString();
    }
    public String toString(Prefixes prefixes) {
        return m_iri.toString(prefixes);
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static NamedIndividual create(String iriString) {
        return create(IRI.create(iriString));
    }
    public static NamedIndividual create(IRI iri) {
        return s_interningManager.intern(new NamedIndividual(iri));
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
