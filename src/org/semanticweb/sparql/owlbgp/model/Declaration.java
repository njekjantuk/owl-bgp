package org.semanticweb.sparql.owlbgp.model;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.sparql.owlbgp.model.Variable.VarType;

public class Declaration extends AbstractAxiom implements ClassAxiom {
    private static final long serialVersionUID = -5136239506197182112L;

    protected static InterningManager<Declaration> s_interningManager=new InterningManager<Declaration>() {
        protected boolean equal(Declaration object1,Declaration object2) {
            if (object1.m_annotations.size()!=object2.m_annotations.size() || object1.m_declaredObject!=object2.m_declaredObject)
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
        protected int getHashCode(Declaration classes) {
            int hashCode=17+classes.m_declaredObject.hashCode();
            for (Annotation anno : classes.m_annotations)
                hashCode+=anno.hashCode();
            return hashCode;
        }
    };
    
    protected final Atomic m_declaredObject;
    
    protected Declaration(Atomic declaredObject,Set<Annotation> annotations) {
        m_declaredObject=declaredObject;
        m_annotations=annotations;
    }
    public ExtendedOWLObject getDeclaredObject() {
        return m_declaredObject;
    }
    public String toString(Prefixes prefixes) {
        StringBuffer buffer=new StringBuffer();
        buffer.append("Declaration(");
        writeAnnoations(buffer, prefixes);
        if (m_declaredObject instanceof Clazz || m_declaredObject instanceof ClassVariable) {
            buffer.append("Class(");
        } else if (m_declaredObject instanceof ObjectProperty || m_declaredObject instanceof ObjectPropertyVariable) {
            buffer.append("ObjectProperty(");
        } else if (m_declaredObject instanceof DataProperty || m_declaredObject instanceof DataPropertyVariable) {
            buffer.append("DataProperty(");
        } else if (m_declaredObject instanceof NamedIndividual || m_declaredObject instanceof IndividualVariable) {
            buffer.append("NamedIndividual(");
        } else if (m_declaredObject instanceof Datatype || m_declaredObject instanceof DatatypeVariable) {
            buffer.append("Datatype(");
        } else if (m_declaredObject instanceof AnnotationProperty) {
            buffer.append("AnnotationProperty(");
        }
        buffer.append(m_declaredObject.toString(prefixes));
        buffer.append("))");
        return buffer.toString();
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static Declaration create(Atomic declaredObject) {
        return Declaration.create(declaredObject,new HashSet<Annotation>());
    }
    public static Declaration create(Atomic declaredObject,Set<Annotation> annotations) {
        return s_interningManager.intern(new Declaration(declaredObject,annotations));
    }
    public <O> O accept(ExtendedOWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
    protected OWLObject convertToOWLAPIObject(OWLAPIConverter converter) {
        return converter.visit(this);
    }
    public Set<Variable> getVariablesInSignature(VarType varType) {
        Set<Variable> variables=new HashSet<Variable>();
        variables.addAll(m_declaredObject.getVariablesInSignature(varType));
        getAnnotationVariables(varType, variables);
        return variables;
    }
    public Set<Variable> getUnboundVariablesInSignature(VarType varType) {
        Set<Variable> variables=new HashSet<Variable>();
        variables.addAll(m_declaredObject.getUnboundVariablesInSignature(varType));
        getUnboundAnnotationVariables(varType, variables);
        return variables;
    }
    public void applyBindings(Map<Variable,Atomic> variablesToBindings) {
        m_declaredObject.applyBindings(variablesToBindings);
    }
    public Axiom getAxiomWithoutAnnotations() {
        return Declaration.create(m_declaredObject);
    }
}
