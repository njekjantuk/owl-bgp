package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.SubClassOf;

public class TPSubClassOfHandler extends TriplePredicateHandler {

    public static int potentiallyConsumedTiples = 0;

    public TPSubClassOfHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.RDFS_SUBCLASS_OF.getIRI());
    }

    public boolean canHandleStreaming(String subject, String predicate, String object) {
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
    public void handleTriple(String subject, String predicate, String object) {
        ClassExpression subClass=translateClassExpression(subject);
        ClassExpression supClass=translateClassExpression(object);
        addAxiom(SubClassOf.create(subClass,supClass));
        consumeTriple(subject, predicate, object);
    }
}
