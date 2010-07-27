package org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.SubClassOf;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.TriplePredicateHandler;

public class TPSubClassOfHandler extends TriplePredicateHandler {

    public TPSubClassOfHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.RDFS_SUBCLASS_OF.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        consumer.addClass(subject);
        consumer.addClass(object);
        return false;
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        consumer.addAxiom(SubClassOf.create(consumer.translateClassExpression(subject),consumer.translateClassExpression(object),consumer.getAnnotations(subject, predicate, object, object)));
        consumer.consumeTriple(subject, predicate, object);
    }
}
