package org.semanticweb.sparql.owlbgp.model.axioms;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.sparql.owlbgp.model.Annotation;
import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.ExtendedOWLObject;
import org.semanticweb.sparql.owlbgp.model.ExtendedOWLObjectVisitor;
import org.semanticweb.sparql.owlbgp.model.ExtendedOWLObjectVisitorEx;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.InterningManager;
import org.semanticweb.sparql.owlbgp.model.OWLAPIConverter;
import org.semanticweb.sparql.owlbgp.model.Prefixes;
import org.semanticweb.sparql.owlbgp.model.Variable;
import org.semanticweb.sparql.owlbgp.model.Variable.VarType;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassVariable;
import org.semanticweb.sparql.owlbgp.model.classexpressions.Clazz;
import org.semanticweb.sparql.owlbgp.model.dataranges.Datatype;
import org.semanticweb.sparql.owlbgp.model.dataranges.DatatypeVariable;
import org.semanticweb.sparql.owlbgp.model.individuals.IndividualVariable;
import org.semanticweb.sparql.owlbgp.model.individuals.NamedIndividual;
import org.semanticweb.sparql.owlbgp.model.properties.AnnotationProperty;
import org.semanticweb.sparql.owlbgp.model.properties.AnnotationPropertyVariable;
import org.semanticweb.sparql.owlbgp.model.properties.DataProperty;
import org.semanticweb.sparql.owlbgp.model.properties.DataPropertyVariable;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectProperty;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyVariable;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;

public class Declaration extends AbstractAxiom {
    private static final long serialVersionUID = -5136239506197182112L;

    protected static InterningManager<Declaration> s_interningManager=new InterningManager<Declaration>() {
        protected boolean equal(Declaration object1,Declaration object2) {
            if (object1.m_annotations.size()!=object2.m_annotations.size() || object1.m_declaredObject!=object2.m_declaredObject)
                return false;
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
        protected int getHashCode(Declaration classes) {
            int hashCode=17+classes.m_declaredObject.hashCode();
            for (Annotation anno : classes.m_annotations)
                hashCode+=anno.hashCode();
            return hashCode;
        }
    };
    
    protected final Atomic m_declaredObject;
    
    protected Declaration(Atomic declaredObject,Set<Annotation> annotations) {
        super(annotations);
        m_declaredObject=declaredObject;
    }
    public ExtendedOWLObject getDeclaredObject() {
        return m_declaredObject;
    }
    public String toString(Prefixes prefixes) {
        StringBuffer buffer=new StringBuffer();
        buffer.append("Declaration(");
        writeAnnoations(buffer, prefixes);
        if (m_declaredObject instanceof Clazz || m_declaredObject instanceof ClassVariable) {
            buffer.append("Class(");
        } else if (m_declaredObject instanceof ObjectProperty || m_declaredObject instanceof ObjectPropertyVariable) {
            buffer.append("ObjectProperty(");
        } else if (m_declaredObject instanceof DataProperty || m_declaredObject instanceof DataPropertyVariable) {
            buffer.append("DataProperty(");
        } else if (m_declaredObject instanceof NamedIndividual || m_declaredObject instanceof IndividualVariable) {
            buffer.append("NamedIndividual(");
        } else if (m_declaredObject instanceof Datatype || m_declaredObject instanceof DatatypeVariable) {
            buffer.append("Datatype(");
        } else if (m_declaredObject instanceof AnnotationProperty || m_declaredObject instanceof AnnotationPropertyVariable) {
            buffer.append("AnnotationProperty(");
        }
        buffer.append(m_declaredObject.toString(prefixes));
        buffer.append("))");
        return buffer.toString();
    }
    @Override
    public String toTurtleString(Prefixes prefixes, Identifier mainNode) {
        Identifier object;
        if (m_declaredObject instanceof Clazz || m_declaredObject instanceof ClassVariable) {
            object=Vocabulary.OWL_CLASS;
        } else if (m_declaredObject instanceof ObjectProperty || m_declaredObject instanceof ObjectPropertyVariable) {
            object=Vocabulary.OWL_OBJECT_PROPERTY;
        } else if (m_declaredObject instanceof DataProperty || m_declaredObject instanceof DataPropertyVariable) {
            object=Vocabulary.OWL_DATA_PROPERTY;
        } else if (m_declaredObject instanceof NamedIndividual || m_declaredObject instanceof IndividualVariable) {
            object=Vocabulary.OWL_NAMED_INDIVIDUAL;
        } else if (m_declaredObject instanceof Datatype || m_declaredObject instanceof DatatypeVariable) {
            object=Vocabulary.RDFS_DATATYPE;
        } else {
            object=Vocabulary.OWL_ANNOTATION_PROPERTY;
        }
        return writeSingleMainTripleAxiom(prefixes, (Atomic)m_declaredObject, Vocabulary.RDF_TYPE, object, m_annotations);
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static Declaration create(Atomic declaredObject) {
        return create(declaredObject,new HashSet<Annotation>());
    }
    public static Declaration create(Atomic declaredObject,Annotation... annotations) {
        Set<Annotation> annos=annotations!=null?new HashSet<Annotation>(Arrays.asList(annotations)):new HashSet<Annotation>();
        return create(declaredObject,annos);
    }
    public static Declaration create(Atomic declaredObject,Set<Annotation> annotations) {
        return s_interningManager.intern(new Declaration(declaredObject,annotations));
    }
    public <O> O accept(ExtendedOWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
    public void accept(ExtendedOWLObjectVisitor visitor) {
        visitor.visit(this);
    }
    protected OWLObject convertToOWLAPIObject(OWLAPIConverter converter) {
        return converter.visit(this);
    }
    public Set<Variable> getVariablesInSignature(VarType varType) {
        Set<Variable> variables=new HashSet<Variable>();
        variables.addAll(m_declaredObject.getVariablesInSignature(varType));
        getAnnotationVariables(varType, variables);
        return variables;
    }
    public ExtendedOWLObject getBoundVersion(Map<Variable,Atomic> variablesToBindings) {
        return create((Atomic)m_declaredObject.getBoundVersion(variablesToBindings),getBoundAnnotations(variablesToBindings));
    }
    public Axiom getAxiomWithoutAnnotations() {
        return create(m_declaredObject);
    }
}
