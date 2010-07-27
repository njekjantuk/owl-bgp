package org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;

public class TypeListHandler extends BuiltInTypeHandler {

    public TypeListHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.RDF_LIST.getIRI());
    }

    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        consumer.consumeTriple(subject, predicate, object); // redundant triple for OWL 1 compatibility
    }
}
