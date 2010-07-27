package org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype;

import org.semanticweb.sparql.owlbgp.model.IRI;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;

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
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
//        consumer.consumeTriple(subject, predicate, object);
//        Identifier annotatedSource=consumer.getResourceObject(subject, Vocabulary.OWL_ANNOTATED_SOURCE.getIRI(), true);
//        Identifier annotatedProperty=consumer.getResourceObject(subject, Vocabulary.OWL_ANNOTATED_PROPERTY.getIRI(), true);
//        Identifier annotatedTarget=consumer.getResourceObject(subject, Vocabulary.OWL_ANNOTATED_TARGET.getIRI(), true);
//        Literal annotatedTargetLiteral=null;
//        if (annotatedTarget==null) annotatedTargetLiteral=consumer.getLiteralObject(subject, Vocabulary.OWL_ANNOTATED_TARGET.getIRI(), true);
//        Set<Annotation> annotations=consumer.translateAnnotations(subject);
//        consumer.setPendingAnnotations(annotations);
//        if (annotatedTarget!=null)
//            consumer.handle(annotatedSource, annotatedProperty, annotatedTarget);
//        else
//            consumer.handle(annotatedSource, annotatedProperty, annotatedTargetLiteral);
//        if (!annotations.isEmpty()) {
//            Axiom ax=consumer.getLastAddedAxiom();
//            consumer.removeAxiom(ax.getAxiomWithoutAnnotations());
//        }
    }
}
