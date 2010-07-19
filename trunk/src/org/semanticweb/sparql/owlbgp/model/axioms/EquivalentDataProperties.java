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
import org.semanticweb.sparql.owlbgp.model.properties.DataPropertyExpression;

public class EquivalentDataProperties extends AbstractAxiom implements ClassAxiom {
    private static final long serialVersionUID = 3226003365814187905L;

    protected static InterningManager<EquivalentDataProperties> s_interningManager=new InterningManager<EquivalentDataProperties>() {
        protected boolean equal(EquivalentDataProperties object1,EquivalentDataProperties object2) {
            if (object1.m_dataPropertyExpressions.size()!=object2.m_dataPropertyExpressions.size()
                    ||object1.m_annotations.size()!=object2.m_annotations.size())
                return false;
            for (DataPropertyExpression ope : object1.m_dataPropertyExpressions) {
                if (!contains(ope, object2.m_dataPropertyExpressions))
                    return false;
            }
            for (Annotation anno : object1.m_annotations) {
                if (!contains(anno, object2.m_annotations))
                    return false;
            } 
            return true;
        }
        protected boolean contains(DataPropertyExpression ope,Set<DataPropertyExpression> opes) {
            for (DataPropertyExpression equiv: opes)
                if (equiv==ope)
                    return true;
            return false;
        }
        protected boolean contains(Annotation annotation,Set<Annotation> annotations) {
            for (Annotation anno : annotations)
                if (anno==annotation)
                    return true;
            return false;
        }
        protected int getHashCode(EquivalentDataProperties object) {
            int hashCode=0;
            for (DataPropertyExpression equiv : object.m_dataPropertyExpressions)
                hashCode+=equiv.hashCode();
            for (Annotation anno : object.m_annotations)
                hashCode+=anno.hashCode();
            return hashCode;
        }
    };
    
    protected final Set<DataPropertyExpression> m_dataPropertyExpressions;
    
    protected EquivalentDataProperties(Set<DataPropertyExpression> dataPropertyExpressions,Set<Annotation> annotations) {
        super(annotations);
        m_dataPropertyExpressions=Collections.unmodifiableSet(dataPropertyExpressions);
    }
    public Set<DataPropertyExpression> getDataPropertyExpressions() {
        return m_dataPropertyExpressions;
    }
    public String toString(Prefixes prefixes) {
        StringBuffer buffer=new StringBuffer();
        buffer.append("EquivalentDataProperties(");
        writeAnnoations(buffer, prefixes);
        boolean notFirst=false;
        for (DataPropertyExpression equiv : m_dataPropertyExpressions) {
            if (notFirst)
                buffer.append(' ');
            else 
                notFirst=true;
            buffer.append(equiv.toString(prefixes));
        }
        buffer.append(")");
        return buffer.toString();
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static EquivalentDataProperties create(DataPropertyExpression... dataPropertyExpressions) {
        return EquivalentDataProperties.create(new HashSet<DataPropertyExpression>(Arrays.asList(dataPropertyExpressions)));
    }
    public static EquivalentDataProperties create(Set<DataPropertyExpression> dataPropertyExpressions) {
        return EquivalentDataProperties.create(dataPropertyExpressions,new HashSet<Annotation>());
    }
    public static EquivalentDataProperties create(Set<DataPropertyExpression> dataPropertyExpressions,Set<Annotation> annotations) {
        return s_interningManager.intern(new EquivalentDataProperties(dataPropertyExpressions,annotations));
    }
    public <O> O accept(ExtendedOWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
    protected OWLObject convertToOWLAPIObject(OWLAPIConverter converter) {
        return converter.visit(this);
    }
    public Set<Variable> getVariablesInSignature(VarType varType) {
        Set<Variable> variables=new HashSet<Variable>();
        for (DataPropertyExpression ope : m_dataPropertyExpressions)
            variables.addAll(ope.getVariablesInSignature(varType));
        getAnnotationVariables(varType, variables);
        return variables;
    }
    public ExtendedOWLObject getBoundVersion(Map<Variable,Atomic> variablesToBindings) {
        Set<DataPropertyExpression> dataPropertyExpression=new HashSet<DataPropertyExpression>();
        for (DataPropertyExpression dpe : m_dataPropertyExpressions) {
            dataPropertyExpression.add((DataPropertyExpression)dpe.getBoundVersion(variablesToBindings));
        }
        return create(dataPropertyExpression, getBoundAnnotations(variablesToBindings));
    }
    public Axiom getAxiomWithoutAnnotations() {
        return create(m_dataPropertyExpressions);
    }
}
