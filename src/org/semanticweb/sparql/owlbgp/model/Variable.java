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
    
    protected Variable(String variable) {
        int begin=0;
        if (variable.startsWith("$") || variable.startsWith("?")) begin=1;
        m_variable=variable.substring(begin).intern();
    }
    public String getVariable() {
        return m_variable;
    }
    public abstract ExtendedOWLObject getBoundVersion(Atomic binding);
    public ExtendedOWLObject getBoundVersion(Map<Variable,? extends Atomic> variablesToBindings) {
        return getBoundVersion(variablesToBindings.get(this));
    }
    @Override
    public String toString(Prefixes prefixes) {
        return m_variable;
    }
    @Override
    public String toTurtleString(Prefixes prefixes,Identifier mainNode) {
        return toString(prefixes);
    }
    public Identifier getIdentifier() {
        return this;
    }
    protected OWLObject convertToOWLAPIObject(ToOWLAPIConverter converter) {
        throw new RuntimeException("An untyped variable cannot have a binding and can, consequently, not be converted into an OWL API pbject and variable "+m_variable+" is untyped. "); 
    }
    public <O> O accept(ExtendedOWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
    public Set<Variable> getVariablesInSignature(VarType varType) {
        return new HashSet<Variable>();
    }
    public boolean isVariable() {
        return true;
    }
}
