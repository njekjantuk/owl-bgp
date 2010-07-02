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


public class DataProperty extends AbstractExtendedOWLObject implements DataPropertyExpression {
    private static final long serialVersionUID = 6601006990377858121L;

    protected final String m_iri;
   
    protected DataProperty(String iri) {
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
    protected static InterningManager<DataProperty> s_interningManager=new InterningManager<DataProperty>() {
        protected boolean equal(DataProperty object1,DataProperty object2) {
            return object1.m_iri.equals(object2.m_iri);
        }
        protected int getHashCode(DataProperty object) {
            return object.m_iri.hashCode();
        }
    };
    public static DataPropertyExpression create(String iri) {
        return s_interningManager.intern(new DataProperty(iri));
    }
    public String getIdentifier() {
        return m_iri;
    }
    public <O> O accept(ExtendedOWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
    public Set<Variable> getVariablesInSignature() {
        return new HashSet<Variable>();
    }
}
