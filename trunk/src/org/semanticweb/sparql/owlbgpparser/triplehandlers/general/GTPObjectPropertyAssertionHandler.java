package org.semanticweb.sparql.owlbgpparser.triplehandlers.general;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.ObjectPropertyAssertion;
import org.semanticweb.sparql.owlbgpparser.TripleConsumer;
import org.semanticweb.sparql.owlbgpparser.Vocabulary;
import org.semanticweb.sparql.owlbgpparser.triplehandlers.AbstractResourceTripleHandler;

public class GTPObjectPropertyAssertionHandler extends AbstractResourceTripleHandler {
    public GTPObjectPropertyAssertionHandler(TripleConsumer consumer) {
        super(consumer);
    }
    public boolean canHandle(Identifier subject, Identifier predicate, Identifier object) {
        return !consumer.isAnnotationProperty(subject) && !Vocabulary.BUILT_IN_VOCABULARY_IRIS.contains(predicate) && !consumer.isOntology(subject);
    }
    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        return false;
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        consumer.consumeTriple(subject, predicate, object);
        consumer.addAxiom(ObjectPropertyAssertion.create(consumer.translateObjectPropertyExpression(predicate),consumer.translateIndividual(subject),consumer.translateIndividual(object)));
    }
}
