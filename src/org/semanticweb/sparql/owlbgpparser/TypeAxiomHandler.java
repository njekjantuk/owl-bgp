package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Axiom;
import org.semanticweb.sparql.owlbgp.model.ILiteral;

public class TypeAxiomHandler extends BuiltInTypeHandler {

    public TypeAxiomHandler(OWLRDFConsumer consumer) {
        super(consumer, OWLRDFVocabulary.OWL_AXIOM.getIRI());
    }
    public TypeAxiomHandler(OWLRDFConsumer consumer, String typeIRI) {
        super(consumer, typeIRI);
    }
    public boolean canHandleStreaming(String subject, String predicate, String object) {
        // We can't handle this is a streaming fashion, because we can't
        // be sure that the subject, predicate, object triples have been parsed.
        return false;
    }
    protected String getTargetTriplePredicate() {
        return OWLRDFVocabulary.OWL_ANNOTATED_TARGET.getIRI();
    }
    protected String getPropertyTriplePredicate() {
        return OWLRDFVocabulary.OWL_ANNOTATED_PROPERTY.getIRI();
    }
    protected String getSourceTriplePredicate() {
        return OWLRDFVocabulary.OWL_ANNOTATED_SOURCE.getIRI();
    }
    public void handleTriple(String subject, String predicate, String object) {
        consumeTriple(subject, predicate, object);
        String annotatedSource=getObjectOfSourceTriple(subject);
        String annotatedProperty=getObjectOfPropertyTriple(subject);
        String annotatedTarget=getObjectOfTargetTriple(subject);
        ILiteral annotatedTargetLiteral=null;
        if (annotatedTarget==null) annotatedTargetLiteral=getTargetLiteral(subject);
        getConsumer().translateAnnotations(subject);
        if (annotatedTarget != null)
            consumer.handle(annotatedSource, annotatedProperty, annotatedTarget);
        else
            consumer.handle(annotatedSource, annotatedProperty, annotatedTargetLiteral);
    }
    protected Axiom handleAxiomTriples(String subjectTriple, String predicateTriple, String objectTriple) {
        return consumer.getLastAddedAxiom();
    }
    protected Axiom handleAxiomTriples(String subjectTripleObject, String predicateTripleObject, ILiteral con) {
        consumer.handle(subjectTripleObject, predicateTripleObject, con);
        return getConsumer().getLastAddedAxiom();
    }
    protected ILiteral getTargetLiteral(String subject) {
        ILiteral con=consumer.getLiteralObject(subject, getTargetTriplePredicate(), true);
        if (con==null) con=getConsumer().getLiteralObject(subject, OWLRDFVocabulary.RDF_OBJECT.getIRI(), true);
        return con;
    }
    protected String  getObjectOfTargetTriple(String  mainNode) {
        String objectTripleObject=consumer.getResourceObject(mainNode, getTargetTriplePredicate(), true);
        if (objectTripleObject==null) objectTripleObject = getConsumer().getResourceObject(mainNode, OWLRDFVocabulary.RDF_OBJECT.getIRI(), true);
        return objectTripleObject;
    }
    protected String getObjectOfPropertyTriple(String subject) {
        String predicateTripleObject=consumer.getResourceObject(subject, getPropertyTriplePredicate(), true);
        if (predicateTripleObject==null) predicateTripleObject = getConsumer().getResourceObject(subject, OWLRDFVocabulary.RDF_PREDICATE.getIRI(), true);
        return predicateTripleObject;
    }
    protected String getObjectOfSourceTriple(String mainNode) {
        String subjectTripleObject=consumer.getResourceObject(mainNode, getSourceTriplePredicate(), true);
        if (subjectTripleObject==null) subjectTripleObject = getConsumer().getResourceObject(mainNode, OWLRDFVocabulary.RDF_SUBJECT.getIRI(), true);
        return subjectTripleObject;
    }
}
