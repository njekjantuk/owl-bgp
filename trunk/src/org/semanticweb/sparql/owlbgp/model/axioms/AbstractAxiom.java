package org.semanticweb.sparql.owlbgp.model.axioms;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.AbstractExtendedOWLObject;
import org.semanticweb.sparql.owlbgp.model.Annotation;
import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.Prefixes;
import org.semanticweb.sparql.owlbgp.model.Variable;
import org.semanticweb.sparql.owlbgp.model.Variable.VarType;

public abstract class AbstractAxiom extends AbstractExtendedOWLObject implements Axiom {
    private static final long serialVersionUID = -1131983400634422022L;

    protected final Set<Annotation> m_annotations;
    
    protected AbstractAxiom(Set<Annotation> annotations) {
        m_annotations=Collections.unmodifiableSet(annotations); //mutable
    }
    public Identifier getIdentifier() {
        return null;
    }
    protected Set<Annotation> getBoundAnnotations(Map<Variable,Atomic> variablesToBindings) {
        Set<Annotation> annotations=new HashSet<Annotation>();
        for (Annotation annotation : m_annotations) 
            annotations.add((Annotation)annotation.getBoundVersion(variablesToBindings));
        return annotations;
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
    public Set<Annotation> getAnnotations() {
        return m_annotations;
    }
}
