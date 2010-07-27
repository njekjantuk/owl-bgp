package org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;

public class TypeAnnotationHandler extends BuiltInTypeHandler {

    public TypeAnnotationHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_ANNOTATION.getIRI());
    }
    
    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
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
