package org.semanticweb.sparql.owlbgp.model;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class AppliedBindingIterator implements Iterator<ExtendedOWLObject>, Iterable<ExtendedOWLObject> {
    protected final ExtendedOWLObject m_extendedOwlObject;
    protected final BindingIterator m_bindingIterator;
    
    public AppliedBindingIterator(ExtendedOWLObject extendedOWLObject,Map<Variable,Set<? extends Atomic>> variablesToBindings) {
        m_extendedOwlObject=extendedOWLObject;
        m_bindingIterator=new BindingIterator(variablesToBindings);
    }
    public boolean hasNext() {
        return m_bindingIterator.hasNext();
    }
    public ExtendedOWLObject next() {
        return m_extendedOwlObject.getBoundVersion(m_bindingIterator.next());
    }
    public void remove() {
        throw new UnsupportedOperationException("The binding iterator does not support removal. ");
    }
    public Iterator<ExtendedOWLObject> iterator() {
        return this;
    }    
}