package org.semanticweb.sparql.owlbgpparser.triplehandlers.rdftype;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgpparser.TripleConsumer;
import org.semanticweb.sparql.owlbgpparser.Vocabulary;

public class TypeOnPropertyHandler extends BuiltInTypeHandler {

    public TypeOnPropertyHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_ON_PROPERTY.getIRI());
    }

    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        consumer.addOnProperty(subject);
        consumer.consumeTriple(subject, predicate, object);
    }
}
