package org.semanticweb.sparql.owlbgpparser.triplehandlers.builtinpredicate;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.InverseObjectProperties;
import org.semanticweb.sparql.owlbgpparser.TripleConsumer;
import org.semanticweb.sparql.owlbgpparser.Vocabulary;
import org.semanticweb.sparql.owlbgpparser.triplehandlers.TriplePredicateHandler;

public class TPInverseOfHandler extends TriplePredicateHandler {

    public TPInverseOfHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_INVERSE_OF.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        consumer.addObjectProperty(subject);
        consumer.addObjectProperty(object);
        return !isSubjectOrObjectAnonymous(subject, object);
    }
    public boolean canHandle(Identifier subject, Identifier predicate, Identifier object) {
        return !isSubjectOrObjectAnonymous(subject, object);
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        consumer.addAxiom(InverseObjectProperties.create(consumer.translateObjectPropertyExpression(subject),consumer.translateObjectPropertyExpression(object)));
        consumer.consumeTriple(subject, predicate, object);
    }
}
