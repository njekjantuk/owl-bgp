package org.semanticweb.sparql.owlbgp.model;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.sparql.owlbgp.model.Variable.VarType;

public class AnnotationAssertion extends AbstractAxiom {
    private static final long serialVersionUID = -2138189482006639179L;

    protected static InterningManager<AnnotationAssertion> s_interningManager=new InterningManager<AnnotationAssertion>() {
        protected boolean equal(AnnotationAssertion object1,AnnotationAssertion object2) {
            if (object1.m_annotationProperty!=object2.m_annotationProperty
                    ||object1.m_annotationSubject!=object2.m_annotationSubject
                    ||object1.m_annotationValue!=object2.m_annotationValue
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
        protected int getHashCode(AnnotationAssertion object) {
            int hashCode=43*object.m_annotationProperty.hashCode()+7*object.m_annotationSubject.hashCode()+11*object.m_annotationValue.hashCode();
            for (Annotation anno : object.m_annotations)
                hashCode+=anno.hashCode();
            return hashCode;
        }
    };
    
    protected final AnnotationPropertyExpression m_annotationProperty;
    protected final AnnotationSubject m_annotationSubject;
    protected final AnnotationValue m_annotationValue;
    
    protected AnnotationAssertion(AnnotationPropertyExpression annotationProperty,AnnotationSubject annotationSubject, AnnotationValue annotationValue, Set<Annotation> annotations) {
        m_annotationProperty=annotationProperty;
        m_annotationSubject=annotationSubject;
        m_annotationValue=annotationValue;
        m_annotations=annotations;
    }
    public AnnotationPropertyExpression getAnnotationProperty() {
        return m_annotationProperty;
    }
    public AnnotationSubject getAnnotationSubject() {
        return m_annotationSubject;
    }
    public AnnotationValue getAnnotationValue() {
        return m_annotationValue;
    }
    public String toString(Prefixes prefixes) {
        StringBuffer buffer=new StringBuffer();
        buffer.append("AnnotationAssertion(");
        writeAnnoations(buffer, prefixes);
        buffer.append(m_annotationProperty.toString(prefixes));
        buffer.append(" ");
        buffer.append(m_annotationSubject.toString(prefixes));
        buffer.append(" ");
        buffer.append(m_annotationValue.toString(prefixes));
        buffer.append(")");
        return buffer.toString();
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static AnnotationAssertion create(AnnotationPropertyExpression annotationProperty,AnnotationSubject annotationSubject, AnnotationValue annotationValue) {
        return AnnotationAssertion.create(annotationProperty,annotationSubject,annotationValue,new HashSet<Annotation>());
    }
    public static AnnotationAssertion create(AnnotationPropertyExpression annotationProperty,AnnotationSubject annotationSubject, AnnotationValue annotationValue, Set<Annotation> annotations) {
        return s_interningManager.intern(new AnnotationAssertion(annotationProperty,annotationSubject,annotationValue,annotations));
    }
    public <O> O accept(ExtendedOWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
    protected OWLObject convertToOWLAPIObject(OWLAPIConverter converter) {
        return converter.visit(this);
    }
    public Set<Variable> getVariablesInSignature(VarType varType) {
        Set<Variable> variables=new HashSet<Variable>();
        variables.addAll(m_annotationProperty.getVariablesInSignature(varType));
        variables.addAll(m_annotationSubject.getVariablesInSignature(varType));
        variables.addAll(m_annotationValue.getVariablesInSignature(varType));
        getAnnotationVariables(varType, variables);
        return variables;
    }
    public Set<Variable> getUnboundVariablesInSignature(VarType varType) {
        Set<Variable> unbound=new HashSet<Variable>();
        unbound.addAll(m_annotationProperty.getUnboundVariablesInSignature(varType));
        unbound.addAll(m_annotationSubject.getUnboundVariablesInSignature(varType));
        unbound.addAll(m_annotationValue.getUnboundVariablesInSignature(varType));
        getUnboundAnnotationVariables(varType, unbound);
        return unbound;
    }
    public void applyBindings(Map<Variable,Atomic> variablesToBindings) {
        m_annotationProperty.applyBindings(variablesToBindings);
        m_annotationSubject.applyBindings(variablesToBindings);
        m_annotationValue.applyBindings(variablesToBindings);
    }
    public Axiom getAxiomWithoutAnnotations() {
        return AnnotationAssertion.create(m_annotationProperty, m_annotationSubject,m_annotationValue);
    }
}
