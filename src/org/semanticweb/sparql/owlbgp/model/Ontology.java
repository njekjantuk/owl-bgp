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

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.sparql.owlbgp.model.Variable.VarType;
import org.semanticweb.sparql.owlbgp.model.axioms.Axiom;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;

public class Ontology extends AbstractExtendedOWLObject {
    private static final long serialVersionUID = 470208281332338403L;

    protected static InterningManager<Ontology> s_interningManager=new InterningManager<Ontology>() {
        protected boolean equal(Ontology object1,Ontology object2) {
            if (object1.m_IRI!=object2.m_IRI) return false;
            for (Identifier id : object1.m_versionIRIs) {
                if (!contains(id, object2.m_versionIRIs))
                    return false;
            }
            for (Import imported : object1.m_directlyImported) {
                if (!contains(imported, object2.m_directlyImported))
                    return false;
            }
            for (Axiom ax : object1.m_axioms) {
                if (!contains(ax, object2.m_axioms))
                    return false;
            }
            for (Annotation anno : object1.m_annotations) {
                if (!contains(anno, object2.m_annotations))
                    return false;
            } 
            return true;
        }
        protected boolean contains(Identifier id,Set<Identifier> ids) {
            for (Identifier ident : ids)
                if (ident==id) return true;
            return false;
        }
        protected boolean contains(Import imported,Set<Import> imports) {
            for (Import imp : imports)
                if (imported==imp) return true;
            return false;
        }
        protected boolean contains(Axiom ax,Set<Axiom> axioms) {
            for (Axiom axiom : axioms)
                if (ax==axiom) return true;
            return false;
        }
        protected boolean contains(Annotation annotation,Set<Annotation> annotations) {
            for (Annotation anno : annotations)
                if (anno==annotation) return true;
            return false;
        }
        protected int getHashCode(Ontology object) {
            int hashCode=1313;
            if (object.m_IRI!=null) hashCode+=object.m_IRI.hashCode();
            for (Identifier id : object.m_versionIRIs) 
                hashCode+=id.hashCode();
            for (Import imported : object.m_directlyImported) 
                hashCode+=imported.hashCode();
            for (Axiom ax : object.m_axioms)
                hashCode+=ax.hashCode();
            for (Annotation anno : object.m_annotations)
                hashCode+=anno.hashCode();
            return hashCode;
        }
    };
    
    protected final Identifier m_IRI;
    protected final Set<Identifier> m_versionIRIs;
    protected final Set<Import> m_directlyImported;
    protected final Set<Annotation> m_annotations;
    protected final Set<Axiom> m_axioms;
    
    protected Ontology(Identifier ontologyIRI, Set<Identifier> versionIRIs, Set<Import> directlyImports, Set<Axiom> axioms, Set<Annotation> ontologyAnnotations) {
        m_IRI=ontologyIRI;
        m_versionIRIs=versionIRIs;
        m_directlyImported=Collections.unmodifiableSet(directlyImports);
        m_axioms=Collections.unmodifiableSet(axioms); 
        m_annotations=Collections.unmodifiableSet(ontologyAnnotations);        
    }
    public boolean containsAxiom(Axiom axiom) {
        return m_axioms.contains(axiom);
    }
    public boolean containsImport(Import imported) {
        return m_directlyImported.contains(imported);
    }
    public boolean containsVersionIRI(Identifier versionIRI) {
        return m_versionIRIs.contains(versionIRI);
    }
    public boolean containsAnnotation(Annotation annotation) {
        return m_annotations.contains(annotation);
    }
    public Identifier getOntologyIRI() {
        return m_IRI;
    }
    public Set<Identifier> getVersionIRIs() {
        return m_versionIRIs;
    }
    public Set<Import> getDirectImports() {
        return m_directlyImported;
    }
    public Set<Annotation> getAnnotations() {
        return m_annotations;
    }
    public Set<Axiom> getAxioms() {
        return m_axioms;
    }
    @Override
    public String toString(Prefixes prefixes) {
        StringBuffer buffer=new StringBuffer();
        buffer.append("Ontology(");
        if (m_IRI!=null) buffer.append(m_IRI);
        for (Identifier id : m_versionIRIs) {
            buffer.append(AbstractExtendedOWLObject.LB);
            buffer.append(id);
        }
        buffer.append(AbstractExtendedOWLObject.LB);
        for (Import imported : m_directlyImported) {
            buffer.append(imported.toString(prefixes));
            buffer.append(AbstractExtendedOWLObject.LB);
        }
        for (Annotation annotation : m_annotations) {
            buffer.append(annotation.toString(prefixes));
            buffer.append(AbstractExtendedOWLObject.LB);
        }
        for (Axiom ax : m_axioms) {
            buffer.append(ax.toString(prefixes));
            buffer.append(AbstractExtendedOWLObject.LB);
        }
        buffer.append(")");
        return buffer.toString();
    }
    @Override
    public String toTurtleString(Prefixes prefixes,Identifier mainNode) {
        StringBuffer buffer=new StringBuffer();
        Identifier ontologyIdentifier;
        if (m_IRI!=null) ontologyIdentifier=m_IRI;
        else ontologyIdentifier=AbstractExtendedOWLObject.getNextBlankNode();
        buffer.append(ontologyIdentifier.toString(prefixes));
        buffer.append(" rdf:type owl:Ontology . ");
        buffer.append(LB);
        for (Identifier id : m_versionIRIs) {
            buffer.append(ontologyIdentifier.toString(prefixes));
            buffer.append(" ");
            buffer.append(Vocabulary.OWL_VERSION_IRI);
            buffer.append(" ");
            buffer.append(id.toString(prefixes));
            buffer.append(" . ");
            buffer.append(LB);
        }
        buffer.append(LB);
        for (Import imported : m_directlyImported) {
            buffer.append(imported.toTurtleString(prefixes,ontologyIdentifier));
            buffer.append(LB);
        }
        for (Annotation annotation : m_annotations) {
            buffer.append(annotation.toTurtleString(prefixes,ontologyIdentifier));
            buffer.append(LB);
        }
        for (Axiom ax : m_axioms) {
            buffer.append(ax.toString(prefixes));
            buffer.append(LB);
        }
        return buffer.toString();
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static Ontology create(Identifier ontologyIRI, Set<Identifier> versionIRIs, Set<Import> directlyImports, Set<Axiom> axioms, Set<Annotation> ontologyAnnotations) {
        return s_interningManager.intern(new Ontology(ontologyIRI,versionIRIs,directlyImports,axioms,ontologyAnnotations));
    }
    public Set<Variable> getVariablesInSignature(VarType varType) {
        Set<Variable> variables=new HashSet<Variable>();
        variables.addAll(m_IRI.getVariablesInSignature(varType));
        for (Identifier id : m_versionIRIs)
            variables.addAll(id.getVariablesInSignature(varType));
        for (Import imported : m_directlyImported)
            variables.addAll(imported.getVariablesInSignature(varType));
        for (Axiom ax : m_axioms)
            variables.addAll(ax.getVariablesInSignature(varType));
        for (Annotation anno : m_annotations)
            variables.addAll(anno.getVariablesInSignature(varType));
        return variables;
    }
    public <O> O accept(ExtendedOWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
    protected OWLObject convertToOWLAPIObject(OWLAPIConverter converter) {
        return converter.visit(this);
    }
    public ExtendedOWLObject getBoundVersion(Map<Variable,Atomic> variablesToBindings) {
        Identifier boundIRI=(m_IRI!=null?(Identifier)m_IRI.getBoundVersion(variablesToBindings):null);
        Set<Identifier> boundVersionIRIs=new HashSet<Identifier>();
        for (Identifier version : m_versionIRIs)
            boundVersionIRIs.add((IRI)version.getBoundVersion(variablesToBindings));
        Set<Import> boundImports=new HashSet<Import>();
        for (Import imported : m_directlyImported)
            boundImports.add((Import)imported.getBoundVersion(variablesToBindings));
        Set<Axiom> boundAxioms=new HashSet<Axiom>();
        for (Axiom ax : m_axioms)
            boundAxioms.add((Axiom)ax.getBoundVersion(variablesToBindings));
        Set<Annotation> boundAnnotations=new HashSet<Annotation>();
        for (Annotation anno : m_annotations)
            boundAnnotations.add((Annotation)anno.getBoundVersion(variablesToBindings));
        return create(boundIRI,boundVersionIRIs,boundImports,boundAxioms,boundAnnotations);
    }
}