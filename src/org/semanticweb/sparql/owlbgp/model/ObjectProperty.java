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


public class ObjectProperty extends AbstractExtendedOWLObject implements ObjectPropertyExpression, Atomic {
    private static final long serialVersionUID = 6601006990377858121L;

    protected static InterningManager<ObjectProperty> s_interningManager=new InterningManager<ObjectProperty>() {
        protected boolean equal(ObjectProperty object1,ObjectProperty object2) {
            return object1.m_iri==object2.m_iri;
        }
        protected int getHashCode(ObjectProperty object) {
            return object.m_iri.hashCode();
        }
    };
    
    public static final ObjectProperty TOP_OBJECT_PROPERTY=create(IRI.create("http://www.w3.org/2002/07/owl#topObjectProperty"));
    public static final ObjectProperty BOTTOM_OBJECT_PROPERTY=create(IRI.create("http://www.w3.org/2002/07/owl#bottomObjectProperty"));
        
    protected final IRI m_iri;
   
    protected ObjectProperty(IRI iri) {
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
    public static ObjectProperty create(String iriString) {
        return create(IRI.create(iriString));
    }
    public static ObjectProperty create(IRI iri) {
        return s_interningManager.intern(new ObjectProperty(iri));
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
