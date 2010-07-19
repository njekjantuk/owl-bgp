package org.semanticweb.sparql.owlbgp.model.properties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.sparql.owlbgp.model.AbstractExtendedOWLObject;
import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.ExtendedOWLObject;
import org.semanticweb.sparql.owlbgp.model.ExtendedOWLObjectVisitorEx;
import org.semanticweb.sparql.owlbgp.model.InterningManager;
import org.semanticweb.sparql.owlbgp.model.OWLAPIConverter;
import org.semanticweb.sparql.owlbgp.model.Prefixes;
import org.semanticweb.sparql.owlbgp.model.Variable;
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
        m_objectPropertyExpressions=Collections.unmodifiableList(objectPropertyExpressions);
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
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static ObjectPropertyChain create(ObjectPropertyExpression... objectPropertyExpressions) {
        return create(Arrays.asList(objectPropertyExpressions));
    }
    public static ObjectPropertyChain create(List<ObjectPropertyExpression> objectPropertyExpressions) {
        return s_interningManager.intern(new ObjectPropertyChain(objectPropertyExpressions));
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
    public ExtendedOWLObject getBoundVersion(Map<Variable,Atomic> variablesToBindings) {
        List<ObjectPropertyExpression> objectPropertyExpressions=new ArrayList<ObjectPropertyExpression>();
        for (ObjectPropertyExpression ope : m_objectPropertyExpressions) {
            objectPropertyExpressions.add((ObjectPropertyExpression)ope.getBoundVersion(variablesToBindings));
        }
        return create(objectPropertyExpressions);
    }
}
