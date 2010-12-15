package org.semanticweb.sparql.owlbgp.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;


public class BindingIterator implements Iterator<Map<Variable,? extends Atomic>>, Iterable<Map<Variable,? extends Atomic>> {
    protected final Variable[] m_variables;
    protected int[] m_currentBindingIndexes;
    protected final Atomic[][] m_variablesToBindings;
    
    public BindingIterator(Map<Variable,Set<? extends Atomic>> variablesToBindings) {
        m_variables=variablesToBindings.keySet().toArray(new Variable[0]);
        m_variablesToBindings=new Atomic[m_variables.length][];
        for (int index=0;index<m_variables.length;index++) {
            m_variablesToBindings[index]=variablesToBindings.get(m_variables[index]).toArray(new Atomic[0]);
        }
    }
    
    public boolean hasNext() {
        if (m_currentBindingIndexes==null) return true;
        for (int index=0;index<m_variables.length;index++) {
            if (m_currentBindingIndexes[index]<m_variablesToBindings[index].length-1) return true;
        }
        return false;
    }
    public Map<Variable,Atomic> next() {
        if (!hasNext()) throw new NoSuchElementException();
        Map<Variable,Atomic> currentBinding=new HashMap<Variable, Atomic>();
        if (m_currentBindingIndexes==null) {
            // first entry, initialize
            m_currentBindingIndexes=new int[m_variables.length];
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
    public Iterator<Map<Variable,? extends Atomic>> iterator() {
        return this;
    }    
}