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
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.sparql.owlbgp.model.Variable.VarType;

public abstract class AbstractExtendedOWLObject implements ExtendedOWLObject {
    private static final long serialVersionUID = -4753012753870470339L;
    
    public static final String LB=System.getProperty("line.separator");
    
    protected AbstractExtendedOWLObject() {}
    public final String toString() {
        return toString(Prefixes.STANDARD_PREFIXES);
    }
    public abstract String toString(Prefixes prefixes);
    public Set<Variable> getVariablesInSignature() {
        return getVariablesInSignature(null);
    }
    public Set<Variable> getVariablesInSignature(VarType varType) {
        return new HashSet<Variable>();
    }
    public Iterable<ExtendedOWLObject> getAppliedBindingsIterator(Map<Variable,Set<Atomic>> variablesToBindings) {
        return new AppliedBindingIterator(this,variablesToBindings);
    }
    public Iterable<OWLObject> getAppliedBindingsOWLAPIIterator(Map<Variable, Set<Atomic>> variablesToBindings,OWLDataFactory dataFactory) {
        return new AppliedBindingOWLAPIIterator(this,variablesToBindings,dataFactory);
    }
    public abstract ExtendedOWLObject getBoundVersion(Map<Variable, Atomic> variablesToBindings);
    public OWLObject getBoundVersion(Map<Variable,Atomic> variablesToBindings,OWLDataFactory dataFactory) {
        return getBoundVersion(variablesToBindings).asOWLAPIObject(dataFactory);
    }
    public abstract <O> O accept(ExtendedOWLObjectVisitorEx<O> visitor);
    public OWLObject asOWLAPIObject(OWLDataFactory dataFactory) {
        return convertToOWLAPIObject(new OWLAPIConverter(dataFactory));
    }
    public OWLObject asOWLAPIObject(OWLAPIConverter converter) {
        return convertToOWLAPIObject(converter);
    }
    protected abstract OWLObject convertToOWLAPIObject(OWLAPIConverter converter);
}