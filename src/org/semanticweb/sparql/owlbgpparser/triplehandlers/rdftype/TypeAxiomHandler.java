package org.semanticweb.sparql.owlbgpparser.triplehandlers.rdftype;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Annotation;
import org.semanticweb.sparql.owlbgp.model.IRI;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.Axiom;
import org.semanticweb.sparql.owlbgp.model.literals.Literal;
import org.semanticweb.sparql.owlbgpparser.TripleConsumer;
import org.semanticweb.sparql.owlbgpparser.Vocabulary;

public class TypeAxiomHandler extends BuiltInTypeHandler {

    public TypeAxiomHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_AXIOM.getIRI());
    }
    public TypeAxiomHandler(TripleConsumer consumer, IRI typeIRI) {
        super(consumer, typeIRI);
    }
    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        // We can't handle this is a streaming fashion, because we can't
        // be sure that the subject, predicate, object triples have been parsed.
        return false;
    }
    protected Identifier getTargetTriplePredicate() {
        return Vocabulary.OWL_ANNOTATED_TARGET.getIRI();
    }
    protected Identifier getPropertyTriplePredicate() {
        return Vocabulary.OWL_ANNOTATED_PROPERTY.getIRI();
    }
    protected Identifier getSourceTriplePredicate() {
        return Vocabulary.OWL_ANNOTATED_SOURCE.getIRI();
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        consumer.consumeTriple(subject, predicate, object);
        Identifier annotatedSource=getObjectOfSourceTriple(subject);
        Identifier annotatedProperty=getObjectOfPropertyTriple(subject);
        Identifier annotatedTarget=getObjectOfTargetTriple(subject);
        Literal annotatedTargetLiteral=null;
        if (annotatedTarget==null) annotatedTargetLiteral=getTargetLiteral(subject);
        Set<Annotation> annotations=consumer.translateAnnotations(subject);
        consumer.setPendingAnnotations(annotations);
        if (annotatedTarget!=null)
            consumer.handle(annotatedSource, annotatedProperty, annotatedTarget);
        else
            consumer.handle(annotatedSource, annotatedProperty, annotatedTargetLiteral);
        if (!annotations.isEmpty()) {
            Axiom ax=consumer.getLastAddedAxiom();
            consumer.removeAxiom(ax.getAxiomWithoutAnnotations());
        }
    }
    protected Axiom handleAxiomTriples(Identifier subjectTriple, Identifier predicateTriple, Identifier objectTriple) {
        return consumer.getLastAddedAxiom();
    }
    protected Axiom handleAxiomTriples(Identifier subjectTripleObject, Identifier predicateTripleObject, Literal con) {
        consumer.handle(subjectTripleObject, predicateTripleObject, con);
        return consumer.getLastAddedAxiom();
    }
    protected Literal getTargetLiteral(Identifier subject) {
        return consumer.getLiteralObject(subject, getTargetTriplePredicate(), true);
    }
    protected Identifier getObjectOfTargetTriple(Identifier  mainNode) {
        return consumer.getResourceObject(mainNode, getTargetTriplePredicate(), true);
    }
    protected Identifier getObjectOfPropertyTriple(Identifier subject) {
        return consumer.getResourceObject(subject, getPropertyTriplePredicate(), true);
    }
    protected Identifier getObjectOfSourceTriple(Identifier mainNode) {
        return consumer.getResourceObject(mainNode, getSourceTriplePredicate(), true);
    }
}
