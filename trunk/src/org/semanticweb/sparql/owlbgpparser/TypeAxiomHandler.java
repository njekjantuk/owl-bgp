package org.semanticweb.sparql.owlbgpparser;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Annotation;
import org.semanticweb.sparql.owlbgp.model.IRI;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.Axiom;
import org.semanticweb.sparql.owlbgp.model.literals.Literal;

public class TypeAxiomHandler extends BuiltInTypeHandler {

    public TypeAxiomHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_AXIOM.getIRI());
    }
    public TypeAxiomHandler(OWLRDFConsumer consumer, IRI typeIRI) {
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
        consumeTriple(subject, predicate, object);
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
        Literal con=consumer.getLiteralObject(subject, getTargetTriplePredicate(), true);
        return con;
    }
    protected Identifier  getObjectOfTargetTriple(Identifier  mainNode) {
        Identifier objectTripleObject=consumer.getResourceObject(mainNode, getTargetTriplePredicate(), true);
        return objectTripleObject;
    }
    protected Identifier getObjectOfPropertyTriple(Identifier subject) {
        Identifier predicateTripleObject=consumer.getResourceObject(subject, getPropertyTriplePredicate(), true);
        return predicateTripleObject;
    }
    protected Identifier getObjectOfSourceTriple(Identifier mainNode) {
        Identifier subjectTripleObject=consumer.getResourceObject(mainNode, getSourceTriplePredicate(), true);
        return subjectTripleObject;
    }
}
