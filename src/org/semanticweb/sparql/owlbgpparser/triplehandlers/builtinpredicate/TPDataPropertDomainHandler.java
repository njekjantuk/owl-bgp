package org.semanticweb.sparql.owlbgpparser.triplehandlers.builtinpredicate;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.DataPropertyDomain;
import org.semanticweb.sparql.owlbgpparser.TripleConsumer;
import org.semanticweb.sparql.owlbgpparser.Vocabulary;
import org.semanticweb.sparql.owlbgpparser.triplehandlers.TriplePredicateHandler;

public class TPDataPropertDomainHandler extends TriplePredicateHandler {

    public TPDataPropertDomainHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_DATA_PROPERTY_DOMAIN.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        return !consumer.isAnonymous(object);
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        consumer.addAxiom(DataPropertyDomain.create(consumer.translateDataPropertyExpression(subject),consumer.translateClassExpression(object)));
        consumer.consumeTriple(subject, predicate, object);
    }
}
