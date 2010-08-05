package org.semanticweb.sparql.owlbgp.model.axioms;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.sparql.owlbgp.model.AbstractExtendedOWLObject;
import org.semanticweb.sparql.owlbgp.model.Annotation;
import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.ExtendedOWLObject;
import org.semanticweb.sparql.owlbgp.model.ExtendedOWLObjectVisitorEx;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.InterningManager;
import org.semanticweb.sparql.owlbgp.model.OWLAPIConverter;
import org.semanticweb.sparql.owlbgp.model.Prefixes;
import org.semanticweb.sparql.owlbgp.model.Variable;
import org.semanticweb.sparql.owlbgp.model.Variable.VarType;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;

public class DisjointObjectProperties extends AbstractAxiom implements ClassAxiom {
    private static final long serialVersionUID = -5653341898298818446L;

    protected static InterningManager<DisjointObjectProperties> s_interningManager=new InterningManager<DisjointObjectProperties>() {
        protected boolean equal(DisjointObjectProperties object1,DisjointObjectProperties object2) {
            if (object1.m_objectPropertyExpressions.size()!=object2.m_objectPropertyExpressions.size()
                    ||object1.m_annotations.size()!=object2.m_annotations.size())
                return false;
            for (ObjectPropertyExpression ope : object1.m_objectPropertyExpressions) {
                if (!contains(ope, object2.m_objectPropertyExpressions))
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
        protected boolean contains(ObjectPropertyExpression ope,Set<ObjectPropertyExpression> opes) {
            for (ObjectPropertyExpression equiv: opes)
                if (equiv==ope)
                    return true;
            return false;
        }
        protected int getHashCode(DisjointObjectProperties object) {
            int hashCode=13;
            for (ObjectPropertyExpression equiv : object.m_objectPropertyExpressions)
                hashCode+=equiv.hashCode();
            for (Annotation anno : object.m_annotations)
                hashCode+=anno.hashCode();
            return hashCode;
        }
    };
    
    protected final Set<ObjectPropertyExpression> m_objectPropertyExpressions;
    
    protected DisjointObjectProperties(Set<ObjectPropertyExpression> objectPropertyExpressions,Set<Annotation> annotations) {
        super(annotations);
        m_objectPropertyExpressions=Collections.unmodifiableSet(objectPropertyExpressions); //mutable
    }
    public Set<ObjectPropertyExpression> getObjectPropertyExpressions() {
        return m_objectPropertyExpressions;
    }
    @Override
    public String toString(Prefixes prefixes) {
        StringBuffer buffer=new StringBuffer();
        buffer.append("DisjointObjectProperties(");
        writeAnnoations(buffer, prefixes);
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
    @Override
    public String toTurtleString(Prefixes prefixes, Identifier mainNode) {
        if (m_objectPropertyExpressions.size()==2) {
            ObjectPropertyExpression ope1=m_objectPropertyExpressions.iterator().next();
            ObjectPropertyExpression ope2=m_objectPropertyExpressions.iterator().next();
            Identifier subject;
            if (!(ope1 instanceof Atomic)) {
                subject=AbstractExtendedOWLObject.getNextBlankNode();
                ope1.toTurtleString(prefixes, subject);
            } else 
                subject=(Atomic)ope1;
            Identifier object;
            if (!(ope2 instanceof Atomic)) {
                object=AbstractExtendedOWLObject.getNextBlankNode();
                ope2.toTurtleString(prefixes, object);
            } else 
                object=(Atomic)ope2;
            return writeSingleMainTripleAxiom(prefixes, subject, Vocabulary.OWL_DISJOINT_OBJECT_PROPERTIES, object, m_annotations);
        } else {
            StringBuffer buffer=new StringBuffer();
            Identifier bnode=AbstractExtendedOWLObject.getNextBlankNode();
            buffer.append(bnode);
            buffer.append(" ");
            buffer.append(Vocabulary.RDF_TYPE.toString(prefixes));
            buffer.append(" ");
            buffer.append(Vocabulary.OWL_ALL_DISJOINT_PROPERTIES.toString(prefixes));
            buffer.append(" . ");
            buffer.append(LB);
            Identifier listMainNode=AbstractExtendedOWLObject.getNextBlankNode();
            buffer.append(bnode);
            buffer.append(" ");
            buffer.append(Vocabulary.OWL_MEMBERS.toString(prefixes));
            buffer.append(" ");
            buffer.append(listMainNode);
            buffer.append(" . ");
            buffer.append(LB);
            ObjectPropertyExpression[] objectPropertyExpressions=m_objectPropertyExpressions.toArray(new ObjectPropertyExpression[0]);
            Identifier[] objectPropertyIDs=new Identifier[objectPropertyExpressions.length];
            for (int i=0;i<objectPropertyExpressions.length;i++) {
                if (objectPropertyExpressions[i] instanceof Atomic)
                    objectPropertyIDs[i]=((Atomic)objectPropertyExpressions[i]).getIdentifier();
                else
                    objectPropertyIDs[i]=AbstractExtendedOWLObject.getNextBlankNode();
            }
            printSequence(buffer, prefixes, listMainNode, objectPropertyIDs);
            for (int i=0;i<objectPropertyExpressions.length;i++)
                if (!(objectPropertyExpressions[i] instanceof Atomic))
                    buffer.append(objectPropertyExpressions[i].toTurtleString(prefixes, objectPropertyIDs[i]));
            for (Annotation anno : m_annotations) 
                anno.toTurtleString(prefixes, bnode);
            return buffer.toString();
        } 
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static DisjointObjectProperties create(Set<ObjectPropertyExpression> objectPropertyExpressions) {
        return create(objectPropertyExpressions,new HashSet<Annotation>());
    }
    public static DisjointObjectProperties create(ObjectPropertyExpression... objectPropertyExpressions) {
        return create(new HashSet<ObjectPropertyExpression>(Arrays.asList(objectPropertyExpressions)),new HashSet<Annotation>());
    }
    public static DisjointObjectProperties create(Set<ObjectPropertyExpression> objectPropertyExpressions,Set<Annotation> annotations) {
        return s_interningManager.intern(new DisjointObjectProperties(objectPropertyExpressions,annotations));
    }
    public <O> O accept(ExtendedOWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
    protected OWLObject convertToOWLAPIObject(OWLAPIConverter converter) {
        return converter.visit(this);
    }
    public Set<Variable> getVariablesInSignature(VarType varType) {
        Set<Variable> variables=new HashSet<Variable>();
        for (ObjectPropertyExpression ope : m_objectPropertyExpressions) 
            variables.addAll(ope.getVariablesInSignature(varType));
        getAnnotationVariables(varType, variables);
        return variables;
    }
    public ExtendedOWLObject getBoundVersion(Map<Variable,Atomic> variablesToBindings) {
        Set<ObjectPropertyExpression> objectPropertyExpression=new HashSet<ObjectPropertyExpression>();
        for (ObjectPropertyExpression ope : m_objectPropertyExpressions) {
            objectPropertyExpression.add((ObjectPropertyExpression)ope.getBoundVersion(variablesToBindings));
        }
        return create(objectPropertyExpression,getBoundAnnotations(variablesToBindings));
    }
    public Axiom getAxiomWithoutAnnotations() {
        return create(m_objectPropertyExpressions);
    }
}
