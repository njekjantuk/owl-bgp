package org.semanticweb.sparql.owlbgp.model;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.sparql.owlbgp.model.Variable.VarType;

public class SubClassOf extends AbstractAxiom implements ClassAxiom {
    private static final long serialVersionUID = 1535222085351189793L;

    protected static InterningManager<SubClassOf> s_interningManager=new InterningManager<SubClassOf>() {
        protected boolean equal(SubClassOf object1,SubClassOf object2) {
            return object1.m_subClass==object2.m_subClass&&object1.m_superClass==object2.m_superClass;
        }
        protected int getHashCode(SubClassOf object) {
            return 7*object.m_subClass.hashCode()+13*object.m_superClass.hashCode();
        }
    };
    
    protected final ClassExpression m_subClass;
    protected final ClassExpression m_superClass;
    
    protected SubClassOf(ClassExpression subClass, ClassExpression superClass) {
        m_subClass=subClass;
        m_superClass=superClass;
    }
    public ClassExpression getSubClassExpression() {
        return m_subClass;
    }
    public ClassExpression getSuperClassExpression() {
        return m_superClass;
    }
    public String toString(Prefixes prefixes) {
        StringBuffer buffer=new StringBuffer();
        buffer.append("SubClassOf(");
        buffer.append(m_subClass.toString(prefixes));
        buffer.append(" ");
        buffer.append(m_superClass.toString(prefixes));
        buffer.append(")");
        return buffer.toString();
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static SubClassOf create(ClassExpression subClass, ClassExpression superClass) {
        return s_interningManager.intern(new SubClassOf(subClass,superClass));
    }
    public <O> O accept(ExtendedOWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
    protected OWLObject convertToOWLAPIObject(OWLAPIConverter converter) {
        return converter.visit(this);
    }
    public Set<Variable> getUnboundVariablesInSignature(VarType varType) {
        Set<Variable> variables=new HashSet<Variable>();
        variables.addAll(m_subClass.getVariablesInSignature(varType));
        variables.addAll(m_superClass.getVariablesInSignature(varType));
        return variables;
    }
    public Set<Variable> getVariablesInSignature(VarType varType) {
        Set<Variable> unbound=new HashSet<Variable>();
        unbound.addAll(m_subClass.getUnboundVariablesInSignature(varType));
        unbound.addAll(m_superClass.getUnboundVariablesInSignature(varType));
        return unbound;
    }
    public void applyBindings(Map<String,String> variablesToBindings) {
        m_subClass.applyBindings(variablesToBindings);
        m_superClass.applyBindings(variablesToBindings);
    }
    public void applyVariableBindings(Map<Variable,ExtendedOWLObject> variablesToBindings) {
        m_subClass.applyVariableBindings(variablesToBindings);
        m_superClass.applyVariableBindings(variablesToBindings);
    }
}
