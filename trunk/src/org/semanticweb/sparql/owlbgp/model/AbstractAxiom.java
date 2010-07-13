package org.semanticweb.sparql.owlbgp.model;

import java.util.Set;

public abstract class AbstractAxiom extends AbstractExtendedOWLObject implements Axiom {
    private static final long serialVersionUID = -1131983400634422022L;

    protected Set<Annotation> m_annotations;
    
    public String getIdentifier() {
        return null;
    }
    public Set<Annotation> getAnnotations() {
        return m_annotations;
    }
    protected void writeAnnoations(StringBuffer buffer,Prefixes prefixes) {
        boolean notFirst=false;
        for (Annotation annotation : m_annotations) {
            if (notFirst) buffer.append(" ");
            else notFirst=true;
            buffer.append(annotation.toString(prefixes));
        }
    }
}
