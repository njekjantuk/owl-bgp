package org.semanticweb.sparql.owlbgp.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.sparql.owlbgp.model.Variable.VarType;

public class EquivalentDataProperties extends AbstractAxiom implements ClassAxiom {
    private static final long serialVersionUID = 3226003365814187905L;

    protected static InterningManager<EquivalentDataProperties> s_interningManager=new InterningManager<EquivalentDataProperties>() {
        protected boolean equal(EquivalentDataProperties opes1,EquivalentDataProperties opes2) {
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
        protected int getHashCode(EquivalentDataProperties equivalentObjectProperties) {
            int hashCode=0;
            for (DataPropertyExpression equiv : equivalentObjectProperties.m_dataPropertyExpressions)
                hashCode+=equiv.hashCode();
            return hashCode;
        }
    };
    
    protected final Set<DataPropertyExpression> m_dataPropertyExpressions;
    
    protected EquivalentDataProperties(Set<DataPropertyExpression> dataPropertyExpressions) {
        m_dataPropertyExpressions=dataPropertyExpressions;
    }
    public Set<DataPropertyExpression> getDataPropertyExpressions() {
        return m_dataPropertyExpressions;
    }
    public String toString(Prefixes prefixes) {
        StringBuffer buffer=new StringBuffer();
        buffer.append("EquivalentDataProperties(");
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
    public static EquivalentDataProperties create(Set<DataPropertyExpression> dataPropertyExpressions) {
        return s_interningManager.intern(new EquivalentDataProperties(dataPropertyExpressions));
    }
    public static EquivalentDataProperties create(DataPropertyExpression... dataPropertyExpressions) {
        return s_interningManager.intern(new EquivalentDataProperties(new HashSet<DataPropertyExpression>(Arrays.asList(dataPropertyExpressions))));
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
