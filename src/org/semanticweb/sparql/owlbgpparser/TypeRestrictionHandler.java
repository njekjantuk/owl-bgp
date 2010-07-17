package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Identifier;

public class TypeRestrictionHandler extends BuiltInTypeHandler {

    public TypeRestrictionHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_RESTRICTION.getIRI());
    }

    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        consumeTriple(subject, predicate, object);
        consumer.addRestriction(subject);
        consumer.addClass(subject);
    }
}
