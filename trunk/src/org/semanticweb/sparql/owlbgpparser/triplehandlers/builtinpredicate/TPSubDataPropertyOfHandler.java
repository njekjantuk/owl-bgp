package org.semanticweb.sparql.owlbgpparser.triplehandlers.builtinpredicate;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.SubDataPropertyOf;
import org.semanticweb.sparql.owlbgpparser.TripleConsumer;
import org.semanticweb.sparql.owlbgpparser.Vocabulary;
import org.semanticweb.sparql.owlbgpparser.triplehandlers.TriplePredicateHandler;

public class TPSubDataPropertyOfHandler extends TriplePredicateHandler {

    public TPSubDataPropertyOfHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_SUB_DATA_PROPERTY_OF.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        return true;
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        consumer.addAxiom(SubDataPropertyOf.create(consumer.translateDataPropertyExpression(subject),consumer.translateDataPropertyExpression(object)));
        consumer.consumeTriple(subject, predicate, object);
    }
}
