package org.semanticweb.sparql.owlbgp.model;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Variable.VarType;

public class Import  {

    protected static InterningManager<Import> s_interningManager=new InterningManager<Import>() {
        protected boolean equal(Import object1,Import object2) {
            return object1.m_iri==object2.m_iri;
        }
        protected int getHashCode(Import classes) {
            return 1011+classes.m_iri.hashCode();
        }
    };
    
    protected final IRI m_iri;
    
    protected Import(IRI iri) {
        m_iri=iri;
    }
    public IRI getImport() {
        return m_iri;
    }
    public String toString(Prefixes prefixes) {
        StringBuffer buffer=new StringBuffer();
        buffer.append("Import(");
        buffer.append(m_iri.toString(prefixes));
        buffer.append(")");
        return buffer.toString();
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static Import create(IRI iri) {
        return s_interningManager.intern(new Import(iri));
    }
//    public <O> O accept(ExtendedOWLObjectVisitorEx<O> visitor) {
//        return visitor.visit(this);
//    }
//    protected OWLObject convertToOWLAPIObject(OWLAPIConverter converter) {
//        return converter.visit(this);
//    }
    public Set<Variable> getVariablesInSignature(VarType varType) {
        return m_iri.getVariablesInSignature(varType);
    }
//    public ExtendedOWLObject getBoundVersion(Map<Variable,Atomic> variablesToBindings) {
//        return create((IRI)m_iri.getBoundVersion(variablesToBindings));
//    }
//    public Axiom getAxiomWithoutAnnotations() {
//        return this;
//    }
}
