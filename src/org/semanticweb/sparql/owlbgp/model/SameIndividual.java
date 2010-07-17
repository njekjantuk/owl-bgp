/* Copyright 2010 by the Oxford University Computing Laboratory
   
   This file is part of OWL-BGP.

   OWL-BGP is free software: you can redistribute it and/or modify
   it under the terms of the GNU Lesser General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.
   
   OWL-BGP is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU Lesser General Public License for more details.
   
   You should have received a copy of the GNU Lesser General Public License
   along with OWL-BGP.  If not, see <http://www.gnu.org/licenses/>.
*/
package org.semanticweb.sparql.owlbgp.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.sparql.owlbgp.model.Variable.VarType;


public class SameIndividual extends AbstractAxiom implements Assertion {
    private static final long serialVersionUID = 5809151112245431871L;

    protected static InterningManager<SameIndividual> s_interningManager=new InterningManager<SameIndividual>() {
        protected boolean equal(SameIndividual object1,SameIndividual object2) {
            if (object1.m_individuals.size()!=object2.m_individuals.size()
                    ||object1.m_annotations.size()!=object2.m_annotations.size())
                return false;
            for (Individual individual : object1.m_individuals) {
                if (!contains(individual, object2.m_individuals))
                    return false;
            }
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
        protected boolean contains(Individual individual ,Set<Individual> individuals) {
            for (Individual ind : individuals)
                if (individual==ind)
                    return true;
            return false;
        }
        protected int getHashCode(SameIndividual object) {
            int hashCode=13;
            for (Individual individual : object.m_individuals)
                hashCode+=individual.hashCode();
            for (Annotation anno : object.m_annotations)
                hashCode+=anno.hashCode();
            return hashCode;
        }
    };
    
    protected final Set<Individual> m_individuals;
   
    protected SameIndividual(Individual... individuals) {
        this(new HashSet<Individual>(Arrays.asList(individuals)),new HashSet<Annotation>());
    }
    protected SameIndividual(Collection<Individual> individuals,Set<Annotation> annotations) {
        m_individuals=new HashSet<Individual>(individuals);
        m_annotations=annotations;
    }
    public Set<Individual> getIndividuals() {
        return m_individuals;
    }
    public String toString(Prefixes prefixes) {
        StringBuffer buffer=new StringBuffer();
        buffer.append("SameIndividual(");
        writeAnnoations(buffer, prefixes);
        boolean notFirst=false;
        for (Individual individual : m_individuals) {
            if (notFirst)
                buffer.append(' ');
            else 
                notFirst=true;
            buffer.append(individual.toString(prefixes));
        }
        buffer.append(")");
        return buffer.toString();
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static SameIndividual create(Individual... individuals) {
        return SameIndividual.create(new HashSet<Individual>(Arrays.asList(individuals)));
    }
    public static SameIndividual create(Set<Individual> individuals) {
        return SameIndividual.create(individuals,new HashSet<Annotation>());
    }
    public static SameIndividual create(Set<Individual> individuals,Set<Annotation> annotations) {
        return s_interningManager.intern(new SameIndividual(individuals,annotations));
    }
    public <O> O accept(ExtendedOWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
    protected OWLObject convertToOWLAPIObject(OWLAPIConverter converter) {
        return converter.visit(this);
    }
    public Set<Variable> getVariablesInSignature(VarType varType) {
        Set<Variable> variables=new HashSet<Variable>();
        for (Individual individual : m_individuals) 
            variables.addAll(individual.getVariablesInSignature(varType));
        getAnnotationVariables(varType, variables);
        return variables;
    }
    public Set<Variable> getUnboundVariablesInSignature(VarType varType) {
        Set<Variable> unbound=new HashSet<Variable>();
        for (Individual individual : m_individuals) 
            unbound.addAll(individual.getUnboundVariablesInSignature(varType));
        getUnboundAnnotationVariables(varType, unbound);
        return unbound;
    }
    public void applyBindings(Map<Variable,Atomic> variablesToBindings) {
        for (Individual individual : m_individuals)
            individual.applyBindings(variablesToBindings);
    }
    public Axiom getAxiomWithoutAnnotations() {
        return SameIndividual.create(m_individuals);
    }
}
