package org.semanticweb.sparql.owlbgpparser.triplehandlers.builtinpredicate;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.SubClassOf;
import org.semanticweb.sparql.owlbgpparser.TripleConsumer;
import org.semanticweb.sparql.owlbgpparser.Vocabulary;
import org.semanticweb.sparql.owlbgpparser.triplehandlers.TriplePredicateHandler;

public class TPSubClassOfHandler extends TriplePredicateHandler {

    public TPSubClassOfHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.RDFS_SUBCLASS_OF.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        if (!consumer.isAnonymous(subject)) {
            if (consumer.isAnonymous(object)) {
                if (consumer.getClassExpressionIfTranslated(object)!=null) return true;
            }
        }
        consumer.addClass(subject);
        consumer.addClass(object);
        return !isSubjectOrObjectAnonymous(subject, object);
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        consumer.addAxiom(SubClassOf.create(consumer.translateClassExpression(subject),consumer.translateClassExpression(object),consumer.getPendingAnnotations()));
        consumer.consumeTriple(subject, predicate, object);
    }
}
