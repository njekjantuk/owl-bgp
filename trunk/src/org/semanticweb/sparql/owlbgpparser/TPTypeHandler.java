package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.ClassAssertion;
import org.semanticweb.sparql.owlbgp.model.Identifier;

public class TPTypeHandler extends TriplePredicateHandler {

    public TPTypeHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.RDF_TYPE.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        // Can handle if object isn't anonymous and either the object
        // is owl:Thing, or it is not part of the build in vocabulary
        return !consumer.isAnonymousNode(object) &&
                (object.equals(Vocabulary.OWL_THING.getIRI()) ||
                        !Vocabulary.BUILT_IN_VOCABULARY_IRIS.contains(object));
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        if (Vocabulary.BUILT_IN_VOCABULARY_IRIS.contains(object)) {
            if (!object.equals(Vocabulary.OWL_THING.getIRI())) {
                // Can't have instance of built in vocabulary!
                // Shall we throw an exception here?
                throw new RuntimeException("Error: individual of builtin type " + object);
            }
        }
        addAxiom(ClassAssertion.create(translateClassExpression(object), translateIndividual(subject)));
        consumeTriple(subject, predicate, object);
    }
}
