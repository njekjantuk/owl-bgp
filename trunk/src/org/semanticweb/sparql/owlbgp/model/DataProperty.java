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


public class DataProperty extends AbstractExtendedOWLObject implements DataPropertyExpression, Atomic {
    private static final long serialVersionUID = 6601006990377858121L;

    protected static InterningManager<DataProperty> s_interningManager=new InterningManager<DataProperty>() {
        protected boolean equal(DataProperty object1,DataProperty object2) {
            return object1.m_iri==object2.m_iri;
        }
        protected int getHashCode(DataProperty object) {
            return object.m_iri.hashCode();
        }
    };
    
    public static final DataProperty TOP_DATA_PROPERTY=create(IRI.create("http://www.w3.org/2002/07/owl#topDataProperty"));
    public static final DataProperty BOTTOM_DATA_PROPERTY=create(IRI.create("http://www.w3.org/2002/07/owl#bottomDataProperty"));
    
    protected final IRI m_iri;
   
    protected DataProperty(IRI iri) {
        m_iri=iri;
    }
    public String getIRIString() {
        return m_iri.m_iri;
    }
    public IRI getIRI() {
        return m_iri;
    }
    public String toString(Prefixes prefixes) {
        return m_iri.toString(prefixes);
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static DataProperty create(String iriString) {
        return create(IRI.create(iriString));
    }
    public static DataProperty create(IRI iri) {
        return s_interningManager.intern(new DataProperty(iri));
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
