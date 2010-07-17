package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Identifier;

public class TypeListHandler extends BuiltInTypeHandler {

    public TypeListHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.RDF_LIST.getIRI());
    }

    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        consumeTriple(subject, predicate, object);
        consumer.addList(subject);
    }
}
