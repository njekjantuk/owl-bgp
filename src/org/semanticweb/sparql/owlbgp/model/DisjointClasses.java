package org.semanticweb.sparql.owlbgp.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.sparql.owlbgp.model.Variable.VarType;

public class DisjointClasses extends AbstractAxiom implements ClassAxiom {
    private static final long serialVersionUID = -2257083759606842880L;

    protected static InterningManager<DisjointClasses> s_interningManager=new InterningManager<DisjointClasses>() {
        protected boolean equal(DisjointClasses classes1,DisjointClasses classes2) {
            if (classes1.m_classExpressions.size()!=classes2.m_classExpressions.size())
                return false;
            for (ClassExpression equiv : classes1.m_classExpressions) {
                if (!contains(equiv, classes2.m_classExpressions))
                    return false;
            } 
            return true;
        }
        protected boolean contains(ClassExpression classExpression,Set<ClassExpression> classExpressions) {
            for (ClassExpression equiv: classExpressions)
                if (equiv==classExpression)
                    return true;
            return false;
        }
        protected int getHashCode(DisjointClasses classes) {
            int hashCode=0;
            for (ClassExpression equiv : classes.m_classExpressions)
                hashCode+=equiv.hashCode();
            return hashCode;
        }
    };
    
    protected final Set<ClassExpression> m_classExpressions;
    
    protected DisjointClasses(Set<ClassExpression> classExpressions) {
        m_classExpressions=classExpressions;
    }
    public Set<ClassExpression> getClassExpressions() {
        return m_classExpressions;
    }
    public String toString(Prefixes prefixes) {
        StringBuffer buffer=new StringBuffer();
        buffer.append("DisjointClasses(");
        boolean notFirst=false;
        for (ClassExpression conjunct : m_classExpressions) {
            if (notFirst)
                buffer.append(' ');
            else 
                notFirst=true;
            buffer.append(conjunct.toString(prefixes));
        }
        buffer.append(")");
        return buffer.toString();
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static DisjointClasses create(Set<ClassExpression> classExpressions) {
        return s_interningManager.intern(new DisjointClasses(classExpressions));
    }
    public static DisjointClasses create(ClassExpression... classExpressions) {
        return s_interningManager.intern(new DisjointClasses(new HashSet<ClassExpression>(Arrays.asList(classExpressions))));
    }
    public <O> O accept(ExtendedOWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
    protected OWLObject convertToOWLAPIObject(OWLAPIConverter converter) {
        return converter.visit(this);
    }
    public Set<Variable> getVariablesInSignature(VarType varType) {
        Set<Variable> variables=new HashSet<Variable>();
        for (ClassExpression classExpression : m_classExpressions) {
            variables.addAll(classExpression.getVariablesInSignature(varType));
        }
        return variables;
    }
    public Set<Variable> getUnboundVariablesInSignature(VarType varType) {
        Set<Variable> variables=new HashSet<Variable>();
        for (ClassExpression classExpression : m_classExpressions) {
            variables.addAll(classExpression.getUnboundVariablesInSignature(varType));
        }
        return variables;
    }
    public void applyBindings(Map<String,String> variablesToBindings) {
        for (ClassExpression ce : m_classExpressions)
            ce.applyBindings(variablesToBindings);
    }
    public void applyVariableBindings(Map<Variable,ExtendedOWLObject> variablesToBindings) {
        for (ClassExpression ce : m_classExpressions)
            ce.applyVariableBindings(variablesToBindings);
    }
}
