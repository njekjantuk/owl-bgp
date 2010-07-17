package org.semanticweb.sparql.owlbgp.model;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Variable.VarType;

public abstract class AbstractAxiom extends AbstractExtendedOWLObject implements Axiom {
    private static final long serialVersionUID = -1131983400634422022L;

    protected Set<Annotation> m_annotations;
    
    public Identifier getIdentifier() {
        return null;
    }
    public Set<Annotation> getAnnotations() {
        return m_annotations;
    }
    protected void writeAnnoations(StringBuffer buffer,Prefixes prefixes) {
        for (Annotation annotation : m_annotations) {
            buffer.append(annotation.toString(prefixes));
            buffer.append(" ");
        }
    }
    protected Set<Variable> getAnnotationVariables(VarType varType, Set<Variable> variables) {
        for (Annotation annotation : m_annotations)
            variables.addAll(annotation.getVariablesInSignature(varType));
        return variables;
    }
    protected Set<Variable> getUnboundAnnotationVariables(VarType varType, Set<Variable> variables) {
        for (Annotation annotation : m_annotations)
            variables.addAll(annotation.getUnboundVariablesInSignature(varType));
        return variables;
    }
}
