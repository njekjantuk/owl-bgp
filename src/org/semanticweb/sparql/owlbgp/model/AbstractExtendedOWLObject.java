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
import org.semanticweb.sparql.owlbgp.model.individuals.AnonymousIndividual;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;

public abstract class AbstractExtendedOWLObject implements ExtendedOWLObject {
    private static final long serialVersionUID = -4753012753870470339L;
    
    public static final String LB=System.getProperty("line.separator");
    
    protected static int nextBlankNodeID=0;
    public static AnonymousIndividual getNextBlankNode() {
        return AnonymousIndividual.create("_:"+(nextBlankNodeID++));
    }
    
    protected AbstractExtendedOWLObject() {}
    public final String toString() {
        return toString(Prefixes.STANDARD_PREFIXES);
    }
    public abstract String toString(Prefixes prefixes);
    public Set<Variable> getVariablesInSignature() {
        return getVariablesInSignature(null);
    }
    public final String toTurtleString(Identifier mainNode) {
        return toTurtleString(Prefixes.STANDARD_PREFIXES, mainNode);
    }
    public abstract String toTurtleString(Prefixes prefixes, Identifier mainNode);
    protected void printSequence(StringBuffer buffer, Prefixes prefixes, Identifier mainNode, Identifier... listIDs) {
        if (listIDs==null) buffer.append(Vocabulary.RDF_NIL.toString(prefixes));
        else if (listIDs.length==1) {
            buffer.append(mainNode);
            buffer.append(" ");
            buffer.append(Vocabulary.RDF_FIRST.toString(prefixes));
            buffer.append(" ");
            buffer.append(listIDs[0]);
            buffer.append(" . ");
            buffer.append(LB);
            buffer.append(mainNode);
            buffer.append(" ");
            buffer.append(Vocabulary.RDF_REST.toString(prefixes));
            buffer.append(" ");
            buffer.append(Vocabulary.RDF_NIL.toString(prefixes));
            buffer.append(" . ");
            buffer.append(LB);
        } else {
            Identifier currentMainNode=mainNode;
            Identifier currentRest=AbstractExtendedOWLObject.getNextBlankNode();
            for (int i=0;i<listIDs.length;i++) {
                buffer.append(currentMainNode);
                buffer.append(" ");
                buffer.append(Vocabulary.RDF_FIRST.toString(prefixes));
                buffer.append(" ");
                buffer.append(listIDs[i].toString(prefixes));
                buffer.append(" . ");
                buffer.append(LB);
                buffer.append(currentMainNode);
                buffer.append(" ");
                buffer.append(Vocabulary.RDF_REST.toString(prefixes));
                buffer.append(" ");
                buffer.append(currentRest.toString(prefixes));
                buffer.append(" . ");
                buffer.append(LB);
                currentMainNode=currentRest;
                if (i<listIDs.length-1) currentRest=AbstractExtendedOWLObject.getNextBlankNode();
                else currentRest=Vocabulary.RDF_NIL;
            }
        }
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