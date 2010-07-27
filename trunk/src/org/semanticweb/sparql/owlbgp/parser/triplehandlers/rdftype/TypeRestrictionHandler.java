package org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;

public class TypeRestrictionHandler extends BuiltInTypeHandler {

    public TypeRestrictionHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_RESTRICTION.getIRI());
    }

    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        consumer.consumeTriple(subject, predicate, object);
        consumer.addRestriction(subject);
        consumer.addClass(subject);
    }
}
