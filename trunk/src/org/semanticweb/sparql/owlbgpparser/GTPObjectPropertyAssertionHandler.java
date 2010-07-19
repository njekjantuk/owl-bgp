package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.ObjectPropertyAssertion;

public class GTPObjectPropertyAssertionHandler extends AbstractResourceTripleHandler {
    public GTPObjectPropertyAssertionHandler(OWLRDFConsumer consumer) {
        super(consumer);
    }
    public boolean canHandle(Identifier subject, Identifier predicate, Identifier object) {
        return !consumer.isAnnotationProperty(subject) && !Vocabulary.BUILT_IN_VOCABULARY_IRIS.contains(predicate) && !getConsumer().isOntology(subject);
    }
    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        return false;
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        consumeTriple(subject, predicate, object);
        addAxiom(ObjectPropertyAssertion.create(translateObjectProperty(predicate), translateIndividual(subject), translateIndividual(object)));
    }
}
