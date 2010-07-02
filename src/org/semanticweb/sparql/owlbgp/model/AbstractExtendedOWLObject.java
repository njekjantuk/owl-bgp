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
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObject;

public abstract class AbstractExtendedOWLObject implements ExtendedOWLObject, Serializable {
    private static final long serialVersionUID = -4753012753870470339L;
    
    public String toString() {
        return toString(Prefixes.STANDARD_PREFIXES);
    }
    public abstract String toString(Prefixes prefixes);
    public Set<Variable> getVariablesInSignature() {
        return getVariablesInSignature(null);
    }
    public Set<Variable> getUnboundVariablesInSignature() {
        return getUnboundVariablesInSignature(null);
    }
    public void applyBindings(Map<String,String> variablesToBindings) {
    }
    public void applyVariableBindings(Map<Variable,ExtendedOWLObject> variablesToBindings) {
    }
    public OWLObject asOWLAPIObject(OWLDataFactory dataFactory) {
        return this.convertToOWLAPIObject(new OWLAPIConverter(dataFactory));
    }
    protected abstract OWLObject convertToOWLAPIObject(OWLAPIConverter converter);
}
