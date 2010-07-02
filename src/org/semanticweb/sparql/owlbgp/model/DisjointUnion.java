package org.semanticweb.sparql.owlbgp.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.sparql.owlbgp.model.Variable.VarType;

public class DisjointUnion extends AbstractAxiom implements ClassAxiom {
    private static final long serialVersionUID = 7822375000293094470L;

    protected static InterningManager<DisjointUnion> s_interningManager=new InterningManager<DisjointUnion>() {
        protected boolean equal(DisjointUnion classes1,DisjointUnion classes2) {
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
        protected int getHashCode(DisjointUnion classes) {
            int hashCode=0;
            for (ClassExpression equiv : classes.m_classExpressions)
                hashCode+=equiv.hashCode();
            return hashCode;
        }
    };
    
    protected final Clazz m_class;
    protected final Set<ClassExpression> m_classExpressions;
    
    protected DisjointUnion(Clazz clazz, Set<ClassExpression> classExpressions) {
        m_class=clazz;
        m_classExpressions=classExpressions;
    }
    public Clazz getClazz() {
        return m_class;
    }
    public Set<ClassExpression> getClassExpressions() {
        return m_classExpressions;
    }
    public String toString(Prefixes prefixes) {
        StringBuffer buffer=new StringBuffer();
        buffer.append("DisjointUnion(");
        buffer.append(m_class.toString(prefixes));
        buffer.append(' ');
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
    public static DisjointUnion create(Clazz clazz,Set<ClassExpression> classExpressions) {
        return s_interningManager.intern(new DisjointUnion(clazz,classExpressions));
    }
    public static DisjointUnion create(Clazz clazz,ClassExpression... classExpressions) {
        return s_interningManager.intern(new DisjointUnion(clazz,new HashSet<ClassExpression>(Arrays.asList(classExpressions))));
    }
    public <O> O accept(ExtendedOWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
    protected OWLObject convertToOWLAPIObject(OWLAPIConverter converter) {
        return converter.visit(this);
    }
    public Set<Variable> getVariablesInSignature(VarType varType) {
        Set<Variable> variables=new HashSet<Variable>();
        variables.addAll(m_class.getVariablesInSignature(varType));
        for (ClassExpression classExpression : m_classExpressions) {
            variables.addAll(classExpression.getVariablesInSignature(varType));
        }
        return variables;
    }
    public Set<Variable> getUnboundVariablesInSignature(VarType varType) {
        Set<Variable> variables=new HashSet<Variable>();
        variables.addAll(m_class.getUnboundVariablesInSignature(varType));
        for (ClassExpression classExpression : m_classExpressions) {
            variables.addAll(classExpression.getUnboundVariablesInSignature(varType));
        }
        return variables;
    }
    public void applyBindings(Map<String,String> variablesToBindings) {
        m_class.applyBindings(variablesToBindings);
        for (ClassExpression ce : m_classExpressions)
            ce.applyBindings(variablesToBindings);
    }
    public void applyVariableBindings(Map<Variable,ExtendedOWLObject> variablesToBindings) {
        m_class.applyVariableBindings(variablesToBindings);
        for (ClassExpression ce : m_classExpressions)
            ce.applyVariableBindings(variablesToBindings);
    }
}
