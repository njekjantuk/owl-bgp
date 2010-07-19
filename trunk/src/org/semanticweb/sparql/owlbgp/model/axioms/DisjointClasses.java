package org.semanticweb.sparql.owlbgp.model.axioms;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.sparql.owlbgp.model.Annotation;
import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.ExtendedOWLObject;
import org.semanticweb.sparql.owlbgp.model.ExtendedOWLObjectVisitorEx;
import org.semanticweb.sparql.owlbgp.model.InterningManager;
import org.semanticweb.sparql.owlbgp.model.OWLAPIConverter;
import org.semanticweb.sparql.owlbgp.model.Prefixes;
import org.semanticweb.sparql.owlbgp.model.Variable;
import org.semanticweb.sparql.owlbgp.model.Variable.VarType;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;

public class DisjointClasses extends AbstractAxiom implements ClassAxiom {
    private static final long serialVersionUID = -2257083759606842880L;

    protected static InterningManager<DisjointClasses> s_interningManager=new InterningManager<DisjointClasses>() {
        protected boolean equal(DisjointClasses classes1,DisjointClasses classes2) {
            if (classes1.m_annotations.size()!=classes2.m_annotations.size() || classes1.m_classExpressions.size()!=classes2.m_classExpressions.size())
                return false;
            for (ClassExpression equiv : classes1.m_classExpressions) {
                if (!contains(equiv, classes2.m_classExpressions))
                    return false;
            }
            for (Annotation anno : classes1.m_annotations) {
                if (!contains(anno, classes2.m_annotations))
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
        protected boolean contains(Annotation annotation,Set<Annotation> annotations) {
            for (Annotation anno : annotations)
                if (anno==annotation)
                    return true;
            return false;
        }
        protected int getHashCode(DisjointClasses classes) {
            int hashCode=0;
            for (ClassExpression equiv : classes.m_classExpressions)
                hashCode+=equiv.hashCode();
            for (Annotation anno : classes.m_annotations)
                hashCode+=anno.hashCode();
            return hashCode;
        }
    };
    
    protected final Set<ClassExpression> m_classExpressions;
    
    protected DisjointClasses(Set<ClassExpression> classExpressions,Set<Annotation> annotations) {
        super(annotations);
        m_classExpressions=Collections.unmodifiableSet(classExpressions); //mutable
    }
    public Set<ClassExpression> getClassExpressions() {
        return m_classExpressions;
    }
    public String toString(Prefixes prefixes) {
        StringBuffer buffer=new StringBuffer();
        buffer.append("DisjointClasses(");
        writeAnnoations(buffer, prefixes);
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
        return create(classExpressions,new HashSet<Annotation>());
    }
    public static DisjointClasses create(ClassExpression... classExpressions) {
        return create(new HashSet<ClassExpression>(Arrays.asList(classExpressions)),new HashSet<Annotation>());
    }
    public static DisjointClasses create(Set<ClassExpression> classExpressions,Set<Annotation> annotations) {
        return s_interningManager.intern(new DisjointClasses(classExpressions,annotations));
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
        getAnnotationVariables(varType, variables);
        return variables;
    }
    public ExtendedOWLObject getBoundVersion(Map<Variable,Atomic> variablesToBindings) {
        Set<ClassExpression> classExpressions=new HashSet<ClassExpression>();
        for (ClassExpression ce : m_classExpressions) {
            classExpressions.add((ClassExpression)ce.getBoundVersion(variablesToBindings));
        }
        return create(classExpressions, getBoundAnnotations(variablesToBindings));
    }
    public Axiom getAxiomWithoutAnnotations() {
        return create(m_classExpressions);
    }
}
