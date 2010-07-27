package org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;

public class TypeDataRangeHandler extends BuiltInTypeHandler {

    public TypeDataRangeHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_DATA_RANGE.getIRI());
    }
    
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        if (!consumer.isAnonymous(subject)) {
            consumer.addDataRange(subject);
            consumer.consumeTriple(subject, predicate, object);
        }
    }
}
