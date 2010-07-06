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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
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
    public Iterable<ExtendedOWLObject> getAppliedBindingsIterator(Map<String,Set<String>> variablesToBindings) {
        return new AppliedBindingIterator(this,variablesToBindings);
    }
    public Iterable<Map<String,String>> getBindingIterator(Map<String,Set<String>> variablesToBindings) {
        return new BindingIterator(variablesToBindings);
    }
    public void applyVariableBindings(Map<Variable,ExtendedOWLObject> variablesToBindings) {
    }
    public OWLObject asOWLAPIObject(OWLDataFactory dataFactory) {
        return this.convertToOWLAPIObject(new OWLAPIConverter(dataFactory));
    }
    protected abstract OWLObject convertToOWLAPIObject(OWLAPIConverter converter);
}

class BindingIterator implements Iterator<Map<String,String>>, Iterable<Map<String,String>> {
    protected final String[] m_variables;
    protected int[] m_currentBindingIndexes;
    protected final String[][] m_variablesToBindings;
    
    public BindingIterator(Map<String,Set<String>> variablesToBindings) {
        m_variables=variablesToBindings.keySet().toArray(new String[0]);
        m_variablesToBindings=new String[m_variables.length][];
        for (int index=0;index<m_variables.length;index++) {
            m_variablesToBindings[index]=variablesToBindings.get(m_variables[index]).toArray(new String[0]);
        }
    }
    
    public boolean hasNext() {
        if (m_currentBindingIndexes==null) return true;
        for (int index=0;index<m_variables.length;index++) {
            if (m_currentBindingIndexes[index]<m_variablesToBindings[index].length-1) return true;
        }
        return false;
    }
    public Map<String,String> next() {
        if (!hasNext()) throw new NoSuchElementException();
        Map<String,String> currentBinding=new HashMap<String, String>();
        if (m_currentBindingIndexes==null) {
            // first entry, initialise
            m_currentBindingIndexes=new int[m_variables.length];
            for (int index=0;index<m_variables.length;index++) {
                m_currentBindingIndexes[index]=0;
            }
        } else {
            boolean flip=false;
            for (int index=m_variables.length-1;index>=0;index--) {
                if (index==m_variables.length-1) {
                    // last bit, always flip
                    if (m_currentBindingIndexes[index]<m_variablesToBindings[index].length-1)
                        m_currentBindingIndexes[index]=m_currentBindingIndexes[index]+1;
                    else {
                        m_currentBindingIndexes[index]=0;
                        flip=true;
                    }
                } else if (flip) {
                    if (m_currentBindingIndexes[index]<m_variablesToBindings[index].length-1) {
                        m_currentBindingIndexes[index]=m_currentBindingIndexes[index]+1;
                        flip=false;
                    } else 
                        m_currentBindingIndexes[index]=0; 
                }
            }
        }
        for (int i=0;i<m_variables.length;i++) {
            currentBinding.put(m_variables[i], m_variablesToBindings[i][m_currentBindingIndexes[i]]);
        }
        return currentBinding;
    }
    public void remove() {
        throw new UnsupportedOperationException("The binding iterator does not support removal. ");
    }
    public Iterator<Map<String,String>> iterator() {
        return this;
    }    
}

class AppliedBindingIterator implements Iterator<ExtendedOWLObject>, Iterable<ExtendedOWLObject> {
    protected final ExtendedOWLObject m_extendedOwlObject;
    protected final BindingIterator m_bindingIterator;
    
    public AppliedBindingIterator(ExtendedOWLObject extendedOwlObject,Map<String,Set<String>> variablesToBindings) {
        m_extendedOwlObject=extendedOwlObject;
        m_bindingIterator=new BindingIterator(variablesToBindings);
    }
    
    public boolean hasNext() {
        return m_bindingIterator.hasNext();
    }
    public ExtendedOWLObject next() {
        m_extendedOwlObject.applyBindings(m_bindingIterator.next());
        return m_extendedOwlObject;
    }
    public void remove() {
        throw new UnsupportedOperationException("The binding iterator does not support removal. ");
    }
    public Iterator<ExtendedOWLObject> iterator() {
        return this;
    }    
}
