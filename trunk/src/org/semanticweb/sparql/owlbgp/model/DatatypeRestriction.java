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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.sparql.owlbgp.model.Variable.VarType;

public class DatatypeRestriction extends AbstractExtendedOWLObject implements DataRange {
    private static final long serialVersionUID = 4586938662095776040L;

    protected static InterningManager<DatatypeRestriction> s_interningManager=new InterningManager<DatatypeRestriction>() {
        protected boolean equal(DatatypeRestriction object1,DatatypeRestriction object2) {
            if (object1.m_datatype!=object2.m_datatype) return false;
            if (object1.m_facetRestrictions.size()!=object2.m_facetRestrictions.size()) return false;
            for (FacetRestriction facetRestriction : object1.m_facetRestrictions) {
                if (!contains(facetRestriction, object2.m_facetRestrictions))
                    return false;
            } 
            return true;
        }
        protected boolean contains(FacetRestriction facetRestriction, Set<FacetRestriction> facetRestrictions) {
            for (FacetRestriction restriction : facetRestrictions)
                if (restriction==facetRestriction)
                    return true;
            return false;
        }
        protected int getHashCode(DatatypeRestriction object) {
            int hashCode=7*object.m_datatype.hashCode();
            for (FacetRestriction restriction : object.m_facetRestrictions)
                hashCode+=restriction.hashCode();
            return hashCode;
        }
    };
    
    protected final Datatype m_datatype;
    protected final Set<FacetRestriction> m_facetRestrictions;
    
    protected DatatypeRestriction(Datatype datatype,Set<FacetRestriction> facetRestrictions) {
        m_datatype=datatype;
        m_facetRestrictions=facetRestrictions;
    }
    public String toString(Prefixes prefixes) {
        StringBuffer buffer=new StringBuffer();
        buffer.append("DatatypeRestriction(");
        buffer.append(m_datatype.toString(prefixes));
        for (FacetRestriction facetRestriction : m_facetRestrictions) {
            buffer.append(facetRestriction.toString(prefixes));
        }
        buffer.append(")");
        return buffer.toString();
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static DatatypeRestriction create(Datatype datatype,Set<FacetRestriction> facetRestrictions) {
        return s_interningManager.intern(new DatatypeRestriction(datatype,facetRestrictions));
    }
    public static DatatypeRestriction create(Datatype datatype,FacetRestriction... facetRestrictions) {
        return s_interningManager.intern(new DatatypeRestriction(datatype,new HashSet<FacetRestriction>(Arrays.asList(facetRestrictions))));
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
        Set<Variable> variables=new HashSet<Variable>();
        variables.addAll(m_datatype.getVariablesInSignature(varType));
        return variables;
    }
    public Set<Variable> getUnboundVariablesInSignature(VarType varType) {
        Set<Variable> unbound=new HashSet<Variable>();
        unbound.addAll(m_datatype.getUnboundVariablesInSignature(varType));
        return unbound;
    }
    public void applyBindings(Map<String,String> variablesToBindings) {
        m_datatype.applyBindings(variablesToBindings);
    }
    public void applyVariableBindings(Map<Variable,ExtendedOWLObject> variablesToBindings) {
        m_datatype.applyVariableBindings(variablesToBindings);
    }
}
