package org.semanticweb.sparql.owlbgp.model;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObject;

public class AppliedBindingOWLAPIIterator implements Iterator<OWLObject>, Iterable<OWLObject> {
    protected final ExtendedOWLObject m_extendedOwlObject;
    protected final OWLDataFactory m_dataFactory;
    protected final BindingIterator m_bindingIterator;
    
    public AppliedBindingOWLAPIIterator(ExtendedOWLObject extendedOWLObject,Map<Variable,Set<Atomic>> variablesToBindings,OWLDataFactory dataFactory) {
        m_extendedOwlObject=extendedOWLObject;
        m_dataFactory=dataFactory;
        m_bindingIterator=new BindingIterator(variablesToBindings);
    }
    public boolean hasNext() {
        return m_bindingIterator.hasNext();
    }
    public OWLObject next() {
        return m_extendedOwlObject.getBoundVersion((m_bindingIterator.next())).asOWLAPIObject(OWLManager.getOWLDataFactory());
    }
    public void remove() {
        throw new UnsupportedOperationException("The binding iterator does not support removal. ");
    }
    public Iterator<OWLObject> iterator() {
        return this;
    }    
}