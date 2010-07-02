package org.semanticweb.sparql.owlbgp.model;

import java.util.Map;

public abstract class Variable extends AbstractExtendedOWLObject {
    private static final long serialVersionUID = -9183189034293387667L;
    
    public static enum VarType {
        CLASS,
        OBJECT_PROPERTY,
        DATA_PROPERTY,
        DATATYPE,
        INDIVIDUAL,
        LITERAL
    }
    
    protected final String m_variable;
    protected String m_binding;
    
    protected Variable(String variable) {
        m_variable=variable.intern();
    }
    public String getVariable() {
        return m_variable;
    }
    public String getBinding() {
        return m_binding;
    }
    public void setBinding(String binding) {
        m_binding=binding;
    }
    public void applyBindings(Map<String,String> variablesToBindings) {
        setBinding(variablesToBindings.get(m_variable));
    }
    public String toString(Prefixes prefixes) {
        if (m_binding!=null) return "("+m_variable+"->"+prefixes.abbreviateIRI(m_binding)+")";
        return m_variable;
    }
    public String getIdentifier() {
        return m_variable;
    }
}
