package org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import java.util.List;
import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Annotation;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.SubObjectPropertyOf;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyChain;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.TriplePredicateHandler;

public class TPPropertyChainAxiomHandler extends TriplePredicateHandler {

    public TPPropertyChainAxiomHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_PROPERTY_CHAIN_AXIOM.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        return false;
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        ObjectPropertyExpression superProp=consumer.translateObjectPropertyExpression(subject);
        List<ObjectPropertyExpression> chain=consumer.translateToObjectPropertyList(object);
        consumer.consumeTriple(subject, predicate, object);
        Set<Annotation> annos=consumer.getAnnotations(subject, predicate, object, (Identifier)Vocabulary.OWL_AXIOM.getIRI());
        consumer.addAxiom(SubObjectPropertyOf.create(ObjectPropertyChain.create(chain),superProp,annos));
    }

}
