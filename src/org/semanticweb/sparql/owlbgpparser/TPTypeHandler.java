package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.ClassAssertion;

public class TPTypeHandler extends TriplePredicateHandler {

    public TPTypeHandler(OWLRDFConsumer consumer) {
        super(consumer, OWLRDFVocabulary.RDF_TYPE.getIRI());
    }

    public boolean canHandleStreaming(String subject, String predicate, String object) {
        // Can handle if object isn't anonymous and either the object
        // String is owl:Thing, or it is not part of the build in vocabulary
        return !consumer.isAnonymousNode(object) &&
                (object.equals(OWLRDFVocabulary.OWL_THING.getIRI()) ||
                        !OWLRDFVocabulary.BUILT_IN_VOCABULARY_IRIS.contains(object));
    }
    public void handleTriple(String subject, String predicate, String object) {
        if (OWLRDFVocabulary.BUILT_IN_VOCABULARY_IRIS.contains(object)) {
            if (!object.equals(OWLRDFVocabulary.OWL_THING.getIRI())) {
                // Can't have instance of built in vocabulary!
                // Shall we throw an exception here?
                throw new RuntimeException("Error: individual of builtin type " + object);
            }
        }
        addAxiom(ClassAssertion.create(translateClassExpression(object), translateIndividual(subject)));
        consumeTriple(subject, predicate, object);
    }
}
