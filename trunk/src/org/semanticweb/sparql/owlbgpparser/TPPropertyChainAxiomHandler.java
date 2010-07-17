package org.semanticweb.sparql.owlbgpparser;

import java.util.List;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.ObjectPropertyChain;
import org.semanticweb.sparql.owlbgp.model.ObjectPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.SubObjectPropertyOf;

public class TPPropertyChainAxiomHandler extends TriplePredicateHandler {

    public TPPropertyChainAxiomHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_PROPERTY_CHAIN_AXIOM.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        return false;
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        ObjectPropertyExpression superProp=consumer.translateObjectPropertyExpression(subject);
        List<ObjectPropertyExpression> chain=consumer.translateToObjectPropertyList(object);
        consumeTriple(subject, predicate, object);
        addAxiom(SubObjectPropertyOf.create(ObjectPropertyChain.create(chain),superProp));
    }

}
