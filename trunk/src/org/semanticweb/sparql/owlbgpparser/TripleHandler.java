package org.semanticweb.sparql.owlbgpparser;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Axiom;
import org.semanticweb.sparql.owlbgp.model.Datatype;
import org.semanticweb.sparql.owlbgp.model.Identifier;

public interface TripleHandler {
    void handlePrefixDirective(String prefixName, Identifier prefix);
    void handleBaseDirective(Identifier base);
    void handleComment(String comment);
    void handleTriple(Identifier subject, Identifier predicate, Identifier object);
    void handleLiteralTriple(Identifier subject, Identifier predicate, String lexicalForm,String langTag, Datatype datatype);
    void handleLiteralTriple(Identifier subject, Identifier predicate, String lexicalForm,Datatype datatype);
    void handleEnd();
    public Set<Axiom> getParsedAxioms();
    public void setClassesInOntologySignature(Set<Identifier> classes);
    public void setObjectPropertiesInOntologySignature(Set<Identifier> objectProperties);
    public void setDataPropertiesInOntologySignature(Set<Identifier> dataProperties);
    public void setIndividualsInOntologySignature(Set<Identifier> individuals);
    public void setCustomDatatypesInOntologySignature(Set<Identifier> customDatatypes);
}
