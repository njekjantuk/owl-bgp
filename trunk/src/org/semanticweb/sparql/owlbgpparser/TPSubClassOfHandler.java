package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.SubClassOf;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;

public class TPSubClassOfHandler extends TriplePredicateHandler {

    public static int potentiallyConsumedTiples = 0;

    public TPSubClassOfHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.RDFS_SUBCLASS_OF.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        if (!consumer.isAnonymousNode(subject)) {
            if (consumer.isAnonymousNode(object)) {
                ClassExpression superClass=consumer.getClassExpressionIfTranslated(object);
                if (superClass != null) {
                    potentiallyConsumedTiples++;
                    return true;
                }
            }
        }
        consumer.addClass(subject);
        consumer.addClass(object);
        return !isSubjectOrObjectAnonymous(subject, object);
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        ClassExpression subClass=translateClassExpression(subject);
        ClassExpression supClass=translateClassExpression(object);
        addAxiom(SubClassOf.create(subClass,supClass,consumer.getPendingAnnotations()));
        consumeTriple(subject, predicate, object);
    }
}
