package org.semanticweb.sparql.owlbgp.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.sparql.owlbgp.model.Variable.VarType;

public class HasKey extends AbstractAxiom {
    private static final long serialVersionUID = -8985552871990493146L;

    protected static InterningManager<HasKey> s_interningManager=new InterningManager<HasKey>() {
        protected boolean equal(HasKey object1,HasKey object2) {
            if (object1.m_classExpression!=object2.m_classExpression||object1.m_objectPropertyExpressions.size()!=object2.m_objectPropertyExpressions.size()||object1.m_dataPropertyExpressions.size()!=object2.m_dataPropertyExpressions.size())
                return false;
            for (ObjectPropertyExpression ope : object1.m_objectPropertyExpressions) {
                if (!contains(ope, object2.m_objectPropertyExpressions))
                    return false;
            }
            for (DataPropertyExpression dpe : object1.m_dataPropertyExpressions) {
                if (!contains(dpe, object2.m_dataPropertyExpressions))
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
        protected int getHashCode(HasKey object) {
            int hashCode=31;
            hashCode+=object.m_classExpression.hashCode();
            for (ObjectPropertyExpression ope : object.m_objectPropertyExpressions)
                hashCode+=ope.hashCode();
            for (DataPropertyExpression dpe : object.m_dataPropertyExpressions)
                hashCode+=dpe.hashCode();
            return hashCode;
        }
    };
    
    protected final ClassExpression m_classExpression;
    protected final Set<ObjectPropertyExpression> m_objectPropertyExpressions;
    protected final Set<DataPropertyExpression> m_dataPropertyExpressions;
    
    protected HasKey(ClassExpression classExpression, Set<PropertyExpression> propertyExpressions) {
        m_classExpression=classExpression;
        Set<ObjectPropertyExpression> objectPropertyExpressions=new HashSet<ObjectPropertyExpression>();
        Set<DataPropertyExpression> dataPropertyExpressions=new HashSet<DataPropertyExpression>();
        for (PropertyExpression pe : propertyExpressions)
            if (pe instanceof ObjectPropertyExpression)
                objectPropertyExpressions.add((ObjectPropertyExpression)pe);
            else
                dataPropertyExpressions.add((DataPropertyExpression)pe);
        m_objectPropertyExpressions=objectPropertyExpressions;
        m_dataPropertyExpressions=dataPropertyExpressions;
    }
    protected HasKey(ClassExpression classExpression, Set<ObjectPropertyExpression> objectPropertyExpressions, Set<DataPropertyExpression> dataPropertyExpressions) {
        m_classExpression=classExpression;
        m_objectPropertyExpressions=objectPropertyExpressions;
        m_dataPropertyExpressions=dataPropertyExpressions;
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
    public static HasKey create(ClassExpression classExpression,Set<PropertyExpression> propertyExpressions) {
        return s_interningManager.intern(new HasKey(classExpression,propertyExpressions));
    }
    public static HasKey create(ClassExpression classExpression,PropertyExpression... propertyExpressions) {
        return s_interningManager.intern(new HasKey(classExpression,new HashSet<PropertyExpression>(Arrays.asList(propertyExpressions))));
    }
    public static HasKey create(ClassExpression classExpression,Set<ObjectPropertyExpression> objectPropertyExpressions,Set<DataPropertyExpression> dataPropertyExpressions) {
        return s_interningManager.intern(new HasKey(classExpression,objectPropertyExpressions,dataPropertyExpressions));
    }
    public static HasKey create(ClassExpression classExpression,ObjectPropertyExpression... objectPropertyExpressions) {
        return s_interningManager.intern(new HasKey(classExpression,new HashSet<ObjectPropertyExpression>(Arrays.asList(objectPropertyExpressions)),new HashSet<DataPropertyExpression>()));
    }
    public static HasKey create(ClassExpression classExpression,DataPropertyExpression... dataPropertyExpressions) {
        return s_interningManager.intern(new HasKey(classExpression,new HashSet<ObjectPropertyExpression>(),new HashSet<DataPropertyExpression>(Arrays.asList(dataPropertyExpressions))));
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
        for (ObjectPropertyExpression ope : m_objectPropertyExpressions) {
            variables.addAll(ope.getVariablesInSignature(varType));
        }
        for (DataPropertyExpression dpe : m_dataPropertyExpressions) {
            variables.addAll(dpe.getVariablesInSignature(varType));
        }
        return variables;
    }
    public Set<Variable> getUnboundVariablesInSignature(VarType varType) {
        Set<Variable> variables=new HashSet<Variable>();
        variables.addAll(m_classExpression.getUnboundVariablesInSignature(varType));
        for (ObjectPropertyExpression ope : m_objectPropertyExpressions) {
            variables.addAll(ope.getUnboundVariablesInSignature(varType));
        }
        for (DataPropertyExpression dpe : m_dataPropertyExpressions) {
            variables.addAll(dpe.getUnboundVariablesInSignature(varType));
        }
        return variables;
    }
    public void applyBindings(Map<String,String> variablesToBindings) {
        m_classExpression.applyBindings(variablesToBindings);
        for (ObjectPropertyExpression ope : m_objectPropertyExpressions)
            ope.applyBindings(variablesToBindings);
        for (DataPropertyExpression dpe : m_dataPropertyExpressions)
            dpe.applyBindings(variablesToBindings);
    }
    public void applyVariableBindings(Map<Variable,ExtendedOWLObject> variablesToBindings) {
        m_classExpression.applyVariableBindings(variablesToBindings);
        for (ObjectPropertyExpression ope : m_objectPropertyExpressions)
            ope.applyVariableBindings(variablesToBindings);
        for (DataPropertyExpression dpe : m_dataPropertyExpressions)
            dpe.applyVariableBindings(variablesToBindings);
    }

}
