package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Identifier;

public class TypeDataRangeHandler extends BuiltInTypeHandler {

    public TypeDataRangeHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_DATA_RANGE.getIRI());
    }
    
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        if (!consumer.isAnonymousNode(subject)) {
            consumer.addDataRange(subject);
            consumeTriple(subject, predicate, object);
        }
    }
}
