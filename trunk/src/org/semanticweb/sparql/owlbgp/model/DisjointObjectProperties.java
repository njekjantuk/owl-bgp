package org.semanticweb.sparql.owlbgp.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.sparql.owlbgp.model.Variable.VarType;

public class DisjointObjectProperties extends AbstractAxiom implements ClassAxiom {
    private static final long serialVersionUID = -5653341898298818446L;

    protected static InterningManager<DisjointObjectProperties> s_interningManager=new InterningManager<DisjointObjectProperties>() {
        protected boolean equal(DisjointObjectProperties opes1,DisjointObjectProperties opes2) {
            if (opes1.m_objectPropertyExpressions.size()!=opes2.m_objectPropertyExpressions.size())
                return false;
            for (ObjectPropertyExpression ope : opes1.m_objectPropertyExpressions) {
                if (!contains(ope, opes2.m_objectPropertyExpressions))
                    return false;
            } 
            return true;
        }
        protected boolean contains(ObjectPropertyExpression ope,Set<ObjectPropertyExpression> opes) {
            for (ObjectPropertyExpression equiv: opes)
                if (equiv==ope)
                    return true;
            return false;
        }
        protected int getHashCode(DisjointObjectProperties opes) {
            int hashCode=13;
            for (ObjectPropertyExpression equiv : opes.m_objectPropertyExpressions)
                hashCode+=equiv.hashCode();
            return hashCode;
        }
    };
    
    protected final Set<ObjectPropertyExpression> m_objectPropertyExpressions;
    
    protected DisjointObjectProperties(Set<ObjectPropertyExpression> objectPropertyExpressions) {
        m_objectPropertyExpressions=objectPropertyExpressions;
    }
    public Set<ObjectPropertyExpression> getObjectPropertyExpressions() {
        return m_objectPropertyExpressions;
    }
    public String toString(Prefixes prefixes) {
        StringBuffer buffer=new StringBuffer();
        buffer.append("DisjointObjectProperties(");
        boolean notFirst=false;
        for (ObjectPropertyExpression equiv : m_objectPropertyExpressions) {
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
    public static DisjointObjectProperties create(Set<ObjectPropertyExpression> objectPropertyExpressions) {
        return s_interningManager.intern(new DisjointObjectProperties(objectPropertyExpressions));
    }
    public static DisjointObjectProperties create(ObjectPropertyExpression... objectPropertyExpressions) {
        return s_interningManager.intern(new DisjointObjectProperties(new HashSet<ObjectPropertyExpression>(Arrays.asList(objectPropertyExpressions))));
    }
    public <O> O accept(ExtendedOWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
    protected OWLObject convertToOWLAPIObject(OWLAPIConverter converter) {
        return converter.visit(this);
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
