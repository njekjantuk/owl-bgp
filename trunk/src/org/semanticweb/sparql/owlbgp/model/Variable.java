package org.semanticweb.sparql.owlbgp.model;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLObject;

public abstract class Variable extends AbstractExtendedOWLObject implements Atomic,Identifier,AnnotationSubject {
    private static final long serialVersionUID = -9183189034293387667L;
    
    public static enum VarType {
        CLASS,
        OBJECT_PROPERTY,
        DATA_PROPERTY,
        ANNOTATION_PROPERTY,
        DATATYPE,
        INDIVIDUAL,
        LITERAL
    }
    
    protected final String m_variable;
    protected Atomic m_binding;
    
    protected Variable(String variable,Atomic binding) {
        m_variable=variable.intern();
        m_binding=binding;
    }
    public String getVariable() {
        return m_variable;
    }
    public Atomic getBinding() {
        return m_binding;
    }
    public abstract void setBinding(Atomic binding);
    public void applyBindings(Map<Variable,Atomic> variablesToBindings) {
        setBinding(variablesToBindings.get(this));
    }
    public String toString(Prefixes prefixes) {
        if (m_binding!=null) return "("+m_variable+"->"+m_binding.toString(prefixes)+")";
        return m_variable;
    }
    public Identifier getIdentifier() {
        return this;
    }
    protected OWLObject convertToOWLAPIObject(OWLAPIConverter converter) {
        throw new RuntimeException("An untyped vriable cannot have a binding and can, consequently, not be converted into an OWL API pbject and variable "+m_variable+" is untyped. "); 
    }
    public <O> O accept(ExtendedOWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
    public Set<Variable> getUnboundVariablesInSignature(VarType varType) {
        return new HashSet<Variable>();
    }
    public Set<Variable> getVariablesInSignature(VarType varType) {
        return new HashSet<Variable>();
    }
}
