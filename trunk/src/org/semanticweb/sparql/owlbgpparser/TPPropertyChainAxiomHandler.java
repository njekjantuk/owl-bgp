package org.semanticweb.sparql.owlbgpparser;

import java.util.List;

import org.semanticweb.sparql.owlbgp.model.ObjectPropertyChain;
import org.semanticweb.sparql.owlbgp.model.ObjectPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.SubObjectPropertyOf;

public class TPPropertyChainAxiomHandler extends TriplePredicateHandler {

    public TPPropertyChainAxiomHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_PROPERTY_CHAIN_AXIOM.getIRI());
    }

    public boolean canHandleStreaming(String subject, String predicate, String object) {
        return false;
    }
    public void handleTriple(String subject, String predicate, String object) {
        ObjectPropertyExpression superProp=consumer.translateObjectPropertyExpression(subject);
        List<ObjectPropertyExpression> chain=consumer.translateToObjectPropertyList(object);
        consumeTriple(subject, predicate, object);
        addAxiom(SubObjectPropertyOf.create(ObjectPropertyChain.create(chain),superProp));
    }

}
