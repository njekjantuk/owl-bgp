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
import org.semanticweb.sparql.owlbgp.model.properties.DataPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.properties.PropertyExpression;

public class HasKey extends AbstractAxiom {
    private static final long serialVersionUID = -8985552871990493146L;

    protected static InterningManager<HasKey> s_interningManager=new InterningManager<HasKey>() {
        protected boolean equal(HasKey object1,HasKey object2) {
            if (object1.m_classExpression!=object2.m_classExpression
                    ||object1.m_objectPropertyExpressions.size()!=object2.m_objectPropertyExpressions.size()
                    ||object1.m_dataPropertyExpressions.size()!=object2.m_dataPropertyExpressions.size()
                    ||object1.m_annotations.size()!=object2.m_annotations.size())
                return false;
            for (ObjectPropertyExpression ope : object1.m_objectPropertyExpressions) {
                if (!contains(ope, object2.m_objectPropertyExpressions))
                    return false;
            }
            for (DataPropertyExpression dpe : object1.m_dataPropertyExpressions) {
                if (!contains(dpe, object2.m_dataPropertyExpressions))
                    return false;
            } 
            for (Annotation anno : object1.m_annotations) {
                if (!contains(anno, object2.m_annotations))
                    return false;
            } 
            return true;
        }
        protected boolean contains(ObjectPropertyExpression ope,Set<ObjectPropertyExpression> opes) {
            for (ObjectPropertyExpression op : opes)
                if (ope==op)
                    return true;
            return false;
        }
        protected boolean contains(DataPropertyExpression dpe,Set<DataPropertyExpression> dpes) {
            for (DataPropertyExpression dp : dpes)
                if (dpe==dp)
                    return true;
            return false;
        }
        protected boolean contains(Annotation annotation,Set<Annotation> annotations) {
            for (Annotation anno : annotations)
                if (anno==annotation)
                    return true;
            return false;
        }
        protected int getHashCode(HasKey object) {
            int hashCode=31;
            hashCode+=object.m_classExpression.hashCode();
            for (ObjectPropertyExpression ope : object.m_objectPropertyExpressions)
                hashCode+=ope.hashCode();
            for (DataPropertyExpression dpe : object.m_dataPropertyExpressions)
                hashCode+=dpe.hashCode();
            for (Annotation anno : object.m_annotations)
                hashCode+=anno.hashCode();
            return hashCode;
        }
    };
    
    protected final ClassExpression m_classExpression;
    protected final Set<ObjectPropertyExpression> m_objectPropertyExpressions;
    protected final Set<DataPropertyExpression> m_dataPropertyExpressions;

    protected HasKey(ClassExpression classExpression, Set<ObjectPropertyExpression> objectPropertyExpressions, Set<DataPropertyExpression> dataPropertyExpressions, Set<Annotation> annotations) {
        super(annotations);
        m_classExpression=classExpression;
        m_objectPropertyExpressions=Collections.unmodifiableSet(objectPropertyExpressions);
        m_dataPropertyExpressions=Collections.unmodifiableSet(dataPropertyExpressions);
    }
    public ClassExpression getClassExpression() {
        return m_classExpression;
    }
    public Set<ObjectPropertyExpression> getObjectPropertyExpressions() {
        return m_objectPropertyExpressions;
    }
    public Set<DataPropertyExpression> getDataPropertyExpressions() {
        return m_dataPropertyExpressions;
    }
    public String toString(Prefixes prefixes) {
        StringBuffer buffer=new StringBuffer();
        buffer.append("HasKey(");
        writeAnnoations(buffer, prefixes);
        buffer.append(m_classExpression.toString(prefixes));
        buffer.append(" (");
        boolean notFirst=false;
        for (ObjectPropertyExpression ope : m_objectPropertyExpressions) {
            if (notFirst)
                buffer.append(" ");
            else 
                notFirst=true;
            buffer.append(ope.toString(prefixes));
        }
        buffer.append(") (");
        notFirst=false;
        for (DataPropertyExpression dpe : m_dataPropertyExpressions) {
            if (notFirst)
                buffer.append(" ");
            else 
                notFirst=true;
            buffer.append(dpe.toString(prefixes));
        }
        buffer.append(")");
        buffer.append(")");
        return buffer.toString();
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static HasKey create(ClassExpression classExpression,PropertyExpression... propertyExpressions) {
        return create(classExpression,new HashSet<PropertyExpression>(Arrays.asList(propertyExpressions)));
    }
    public static HasKey create(ClassExpression classExpression,Set<PropertyExpression> propertyExpressions) {
        return create(classExpression,propertyExpressions,new HashSet<Annotation>());
    }
    public static HasKey create(ClassExpression classExpression,Set<PropertyExpression> propertyExpressions,Set<Annotation> annotations) {
        Set<ObjectPropertyExpression> objectPropertyExpressions=new HashSet<ObjectPropertyExpression>();
        Set<DataPropertyExpression> dataPropertyExpressions=new HashSet<DataPropertyExpression>();
        for (PropertyExpression pe : propertyExpressions)
            if (pe instanceof ObjectPropertyExpression)
                objectPropertyExpressions.add((ObjectPropertyExpression)pe);
            else
                dataPropertyExpressions.add((DataPropertyExpression)pe);
        return create(classExpression,objectPropertyExpressions,dataPropertyExpressions,annotations);
    }
    public static HasKey create(ClassExpression classExpression,Set<ObjectPropertyExpression> objectPropertyExpressions,Set<DataPropertyExpression> dataPropertyExpressions,Set<Annotation> annotations) {
        return s_interningManager.intern(new HasKey(classExpression,objectPropertyExpressions,dataPropertyExpressions,annotations));
    }
    public <O> O accept(ExtendedOWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
    protected OWLObject convertToOWLAPIObject(OWLAPIConverter converter) {
        return converter.visit(this);
    }
    public Set<Variable> getVariablesInSignature(VarType varType) {
        Set<Variable> variables=new HashSet<Variable>();
        variables.addAll(m_classExpression.getVariablesInSignature(varType));
        for (ObjectPropertyExpression ope : m_objectPropertyExpressions)
            variables.addAll(ope.getVariablesInSignature(varType));
        for (DataPropertyExpression dpe : m_dataPropertyExpressions)
            variables.addAll(dpe.getVariablesInSignature(varType));
        getAnnotationVariables(varType, variables);
        return variables;
    }
    public ExtendedOWLObject getBoundVersion(Map<Variable,Atomic> variablesToBindings) {
        Set<ObjectPropertyExpression> objectPropertyExpressions=new HashSet<ObjectPropertyExpression>();
        for (ObjectPropertyExpression ope : m_objectPropertyExpressions) {
            objectPropertyExpressions.add((ObjectPropertyExpression)ope.getBoundVersion(variablesToBindings));
        }
        Set<DataPropertyExpression> dataPropertyExpressions=new HashSet<DataPropertyExpression>();
        for (DataPropertyExpression dpe : m_dataPropertyExpressions) {
            dataPropertyExpressions.add((DataPropertyExpression)dpe.getBoundVersion(variablesToBindings));
        }
        return create((ClassExpression)m_classExpression.getBoundVersion(variablesToBindings),objectPropertyExpressions,dataPropertyExpressions,getBoundAnnotations(variablesToBindings));
    }
    public Axiom getAxiomWithoutAnnotations() {
        return create(m_classExpression, m_objectPropertyExpressions, m_dataPropertyExpressions, new HashSet<Annotation>());
    }
}
