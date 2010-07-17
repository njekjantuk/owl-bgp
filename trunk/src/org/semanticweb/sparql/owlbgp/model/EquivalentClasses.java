package org.semanticweb.sparql.owlbgp.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.sparql.owlbgp.model.Variable.VarType;

public class EquivalentClasses extends AbstractAxiom implements ClassAxiom {
    private static final long serialVersionUID = -5653341898298818446L;

    protected static InterningManager<EquivalentClasses> s_interningManager=new InterningManager<EquivalentClasses>() {
        protected boolean equal(EquivalentClasses object1,EquivalentClasses object2) {
            if (object1.m_classExpressions.size()!=object2.m_classExpressions.size()
                    ||object1.m_annotations.size()!=object2.m_annotations.size())
                return false;
            for (ClassExpression equiv : object1.m_classExpressions) {
                if (!contains(equiv, object2.m_classExpressions))
                    return false;
            } 
            for (Annotation anno : object1.m_annotations) {
                if (!contains(anno, object2.m_annotations))
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
        protected int getHashCode(EquivalentClasses object) {
            int hashCode=0;
            for (ClassExpression equiv : object.m_classExpressions)
                hashCode+=equiv.hashCode();
            for (Annotation anno : object.m_annotations)
                hashCode+=anno.hashCode();
            return hashCode;
        }
    };
    
    protected final Set<ClassExpression> m_classExpressions;
    
    protected EquivalentClasses(Set<ClassExpression> classExpressions,Set<Annotation> annotations) {
        m_classExpressions=classExpressions;
        m_annotations=annotations;
    }
    public Set<ClassExpression> getClassExpressions() {
        return m_classExpressions;
    }
    public String toString(Prefixes prefixes) {
        StringBuffer buffer=new StringBuffer();
        buffer.append("EquivalentClasses(");
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
    public static EquivalentClasses create(Set<ClassExpression> classExpressions) {
        return EquivalentClasses.create(classExpressions,new HashSet<Annotation>());
    }
    public static EquivalentClasses create(ClassExpression... classExpressions) {
        return EquivalentClasses.create(new HashSet<ClassExpression>(Arrays.asList(classExpressions)),new HashSet<Annotation>());
    }
    public static EquivalentClasses create(Set<ClassExpression> classExpressions,Set<Annotation> annotations) {
        return s_interningManager.intern(new EquivalentClasses(classExpressions,annotations));
    }
    public <O> O accept(ExtendedOWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
    protected OWLObject convertToOWLAPIObject(OWLAPIConverter converter) {
        return converter.visit(this);
    }
    public Set<Variable> getVariablesInSignature(VarType varType) {
        Set<Variable> variables=new HashSet<Variable>();
        for (ClassExpression classExpression : m_classExpressions) 
            variables.addAll(classExpression.getVariablesInSignature(varType));
        getAnnotationVariables(varType, variables);
        return variables;
    }
    public Set<Variable> getUnboundVariablesInSignature(VarType varType) {
        Set<Variable> variables=new HashSet<Variable>();
        for (ClassExpression classExpression : m_classExpressions) 
            variables.addAll(classExpression.getUnboundVariablesInSignature(varType));
        getUnboundAnnotationVariables(varType, variables);
        return variables;
    }
    public void applyBindings(Map<Variable,Atomic> variablesToBindings) {
        for (ClassExpression ce : m_classExpressions)
            ce.applyBindings(variablesToBindings);
    }
    public Axiom getAxiomWithoutAnnotations() {
        return EquivalentClasses.create(m_classExpressions);
    }
}
