package org.semanticweb.sparql.owlbgp.model;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.sparql.owlbgp.model.Variable.VarType;
import org.semanticweb.sparql.owlbgp.model.axioms.AbstractAxiom;
import org.semanticweb.sparql.owlbgp.model.axioms.Axiom;

public class Import extends AbstractAxiom {
    private static final long serialVersionUID = 6105810455730607025L;

    protected static InterningManager<Import> s_interningManager=new InterningManager<Import>() {
        protected boolean equal(Import object1,Import object2) {
            return object1.m_identifier==object2.m_identifier;
        }
        protected int getHashCode(Import classes) {
            return 1011+classes.m_identifier.hashCode();
        }
    };
    
    protected final Identifier m_identifier;
    
    protected Import(Identifier identifier) {
        super(new HashSet<Annotation>());
        m_identifier=identifier;
    }
    public Identifier getImport() {
        return m_identifier;
    }
    public String toString(Prefixes prefixes) {
        StringBuffer buffer=new StringBuffer();
        buffer.append("Import(");
        buffer.append(m_identifier.toString(prefixes));
        buffer.append(")");
        return buffer.toString();
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static Import create(Identifier identifier) {
        return s_interningManager.intern(new Import(identifier));
    }
    public <O> O accept(ExtendedOWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
    protected OWLObject convertToOWLAPIObject(OWLAPIConverter converter) {
        return converter.visit(this);
    }
    public Set<Variable> getVariablesInSignature(VarType varType) {
        return m_identifier.getVariablesInSignature(varType);
    }
    public ExtendedOWLObject getBoundVersion(Map<Variable,Atomic> variablesToBindings) {
        return create((IRI)m_identifier.getBoundVersion(variablesToBindings));
    }
    public Axiom getAxiomWithoutAnnotations() {
        return this;
    }
}
