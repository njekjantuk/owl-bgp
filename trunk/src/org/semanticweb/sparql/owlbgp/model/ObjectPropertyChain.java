package org.semanticweb.sparql.owlbgp.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.sparql.owlbgp.model.Variable.VarType;

public class ObjectPropertyChain extends AbstractExtendedOWLObject implements ObjectPropertyExpression {
    private static final long serialVersionUID = 3523597422428066846L;

    protected static InterningManager<ObjectPropertyChain> s_interningManager=new InterningManager<ObjectPropertyChain>() {
        protected boolean equal(ObjectPropertyChain object1,ObjectPropertyChain object2) {
            if (object1.m_objectPropertyExpressions.size()!=object2.m_objectPropertyExpressions.size())
                return false;
            for (ObjectPropertyExpression ope : object1.m_objectPropertyExpressions) {
                if (!contains(ope, object2.m_objectPropertyExpressions))
                    return false;
            }
            return true;
        }
        protected boolean contains(ObjectPropertyExpression ope,List<ObjectPropertyExpression> opes) {
            for (ObjectPropertyExpression op : opes)
                if (ope==op)
                    return true;
            return false;
        }
        protected int getHashCode(ObjectPropertyChain object) {
            int hashCode=43;
            for (ObjectPropertyExpression ope : object.m_objectPropertyExpressions)
                hashCode+=ope.hashCode();
            return hashCode;
        }
    };
    
    protected final List<ObjectPropertyExpression> m_objectPropertyExpressions;
    
    protected ObjectPropertyChain(List<ObjectPropertyExpression> objectPropertyExpressions) {
        m_objectPropertyExpressions=objectPropertyExpressions;
    }
    public List<ObjectPropertyExpression> getObjectPropertyExpressions() {
        return m_objectPropertyExpressions;
    }
    public String toString(Prefixes prefixes) {
        StringBuffer buffer=new StringBuffer();
        buffer.append("ObjectPropertyChain(");
        boolean notFirst=false;
        for (ObjectPropertyExpression ope : m_objectPropertyExpressions) {
            if (notFirst)
                buffer.append(" ");
            else 
                notFirst=true;
            buffer.append(ope.toString(prefixes));
        }
        buffer.append(")");
        return buffer.toString();
    }
    public String getIdentifier() {
        return null;
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static ObjectPropertyChain create(List<ObjectPropertyExpression> objectPropertyExpressions) {
        return s_interningManager.intern(new ObjectPropertyChain(objectPropertyExpressions));
    }
    public static ObjectPropertyChain create(ObjectPropertyExpression... objectPropertyExpressions) {
        return s_interningManager.intern(new ObjectPropertyChain(Arrays.asList(objectPropertyExpressions)));
    }
    public <O> O accept(ExtendedOWLObjectVisitorEx<O> visitor) {
        return null;
    }
    protected OWLObject convertToOWLAPIObject(OWLAPIConverter converter) {
        return null;
    }
    public Set<Variable> getVariablesInSignature(VarType varType) {
        Set<Variable> variables=new HashSet<Variable>();
        for (ObjectPropertyExpression ope : m_objectPropertyExpressions) {
            variables.addAll(ope.getVariablesInSignature(varType));
        }
        return variables;
    }
    public Set<Variable> getUnboundVariablesInSignature(VarType varType) {
        Set<Variable> variables=new HashSet<Variable>();
        for (ObjectPropertyExpression ope : m_objectPropertyExpressions) {
            variables.addAll(ope.getUnboundVariablesInSignature(varType));
        }
        return variables;
    }
    public void applyBindings(Map<String,String> variablesToBindings) {
        for (ObjectPropertyExpression ope : m_objectPropertyExpressions)
            ope.applyBindings(variablesToBindings);
    }
    public void applyVariableBindings(Map<Variable,ExtendedOWLObject> variablesToBindings) {
        for (ObjectPropertyExpression ope : m_objectPropertyExpressions)
            ope.applyVariableBindings(variablesToBindings);
    }
}
