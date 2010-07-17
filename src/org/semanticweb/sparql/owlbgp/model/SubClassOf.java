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
            if (object1.m_subClass!=object2.m_subClass
                    ||object1.m_superClass!=object2.m_superClass
                    ||object1.m_annotations.size()!=object2.m_annotations.size())
                return false;
            for (Annotation anno : object1.m_annotations) {
                if (!contains(anno, object2.m_annotations))
                    return false;
            } 
            return true;
        }
        protected boolean contains(Annotation annotation,Set<Annotation> annotations) {
            for (Annotation anno : annotations)
                if (anno==annotation)
                    return true;
            return false;
        }
        protected int getHashCode(SubClassOf object) {
            int hashCode=7*object.m_subClass.hashCode()+13*object.m_superClass.hashCode();
            for (Annotation anno : object.m_annotations)
                hashCode+=anno.hashCode();
            return hashCode;
        }
    };
    
    protected final ClassExpression m_subClass;
    protected final ClassExpression m_superClass;
    
    protected SubClassOf(ClassExpression subClass, ClassExpression superClass,Set<Annotation> annotations) {
        m_subClass=subClass;
        m_superClass=superClass;
        m_annotations=annotations;
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
        writeAnnoations(buffer, prefixes);
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
        return SubClassOf.create(subClass,superClass,new HashSet<Annotation>());
    }
    public static SubClassOf create(ClassExpression subClass, ClassExpression superClass,Set<Annotation> annotations) {
        return s_interningManager.intern(new SubClassOf(subClass,superClass,annotations));
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
        getAnnotationVariables(varType, variables);
        return variables;
    }
    public Set<Variable> getVariablesInSignature(VarType varType) {
        Set<Variable> unbound=new HashSet<Variable>();
        unbound.addAll(m_subClass.getUnboundVariablesInSignature(varType));
        unbound.addAll(m_superClass.getUnboundVariablesInSignature(varType));
        getUnboundAnnotationVariables(varType, unbound);
        return unbound;
    }
    public void applyBindings(Map<Variable,Atomic> variablesToBindings) {
        m_subClass.applyBindings(variablesToBindings);
        m_superClass.applyBindings(variablesToBindings);
    }
    public Axiom getAxiomWithoutAnnotations() {
        return SubClassOf.create(m_subClass, m_superClass);
    }
}
