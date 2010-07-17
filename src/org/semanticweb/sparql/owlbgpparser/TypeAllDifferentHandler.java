package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Identifier;

public class TypeAllDifferentHandler extends BuiltInTypeHandler {

    public TypeAllDifferentHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_ALL_DIFFERENT.getIRI());
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        consumeTriple(subject, predicate, object);
    }
}
