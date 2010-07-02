package org.semanticweb.sparql.owlbgp.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.sparql.owlbgp.model.Variable.VarType;

public class DisjointDataProperties extends AbstractAxiom implements ClassAxiom {
    private static final long serialVersionUID = -9059227935467720333L;

    protected static InterningManager<DisjointDataProperties> s_interningManager=new InterningManager<DisjointDataProperties>() {
        protected boolean equal(DisjointDataProperties opes1,DisjointDataProperties opes2) {
            if (opes1.m_dataPropertyExpressions.size()!=opes2.m_dataPropertyExpressions.size())
                return false;
            for (DataPropertyExpression ope : opes1.m_dataPropertyExpressions) {
                if (!contains(ope, opes2.m_dataPropertyExpressions))
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
        protected int getHashCode(DisjointDataProperties opes) {
            int hashCode=53;
            for (DataPropertyExpression equiv : opes.m_dataPropertyExpressions)
                hashCode+=equiv.hashCode();
            return hashCode;
        }
    };
    
    protected final Set<DataPropertyExpression> m_dataPropertyExpressions;
    
    protected DisjointDataProperties(Set<DataPropertyExpression> dataPropertyExpressions) {
        m_dataPropertyExpressions=dataPropertyExpressions;
    }
    public Set<DataPropertyExpression> getDataPropertyExpressions() {
        return m_dataPropertyExpressions;
    }
    public String toString(Prefixes prefixes) {
        StringBuffer buffer=new StringBuffer();
        buffer.append("DisjointDataProperties(");
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
    public static DisjointDataProperties create(Set<DataPropertyExpression> dataPropertyExpressions) {
        return s_interningManager.intern(new DisjointDataProperties(dataPropertyExpressions));
    }
    public static DisjointDataProperties create(DataPropertyExpression... dataPropertyExpressions) {
        return s_interningManager.intern(new DisjointDataProperties(new HashSet<DataPropertyExpression>(Arrays.asList(dataPropertyExpressions))));
    }
    public <O> O accept(ExtendedOWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
    protected OWLObject convertToOWLAPIObject(OWLAPIConverter converter) {
        return converter.visit(this);
    }
    public Set<Variable> getVariablesInSignature(VarType varType) {
        Set<Variable> variables=new HashSet<Variable>();
        for (DataPropertyExpression ope : m_dataPropertyExpressions) {
            variables.addAll(ope.getVariablesInSignature(varType));
        }
        return variables;
    }
    public Set<Variable> getUnboundVariablesInSignature(VarType varType) {
        Set<Variable> variables=new HashSet<Variable>();
        for (DataPropertyExpression ope : m_dataPropertyExpressions) {
            variables.addAll(ope.getUnboundVariablesInSignature(varType));
        }
        return variables;
    }
    public void applyBindings(Map<String,String> variablesToBindings) {
        for (DataPropertyExpression ope : m_dataPropertyExpressions)
            ope.applyBindings(variablesToBindings);
    }
    public void applyVariableBindings(Map<Variable,ExtendedOWLObject> variablesToBindings) {
        for (DataPropertyExpression ope : m_dataPropertyExpressions)
            ope.applyVariableBindings(variablesToBindings);
    }
}
