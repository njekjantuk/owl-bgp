package org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.TriplePredicateHandler;

public class TPVersionIRIHandler extends TriplePredicateHandler {

    public TPVersionIRIHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_VERSION_IRI.getIRI());
    }

    public void handleTriple(Identifier subject,Identifier predicate,Identifier object) {
        if (!(consumer.isAnonymous(object))) {
            consumer.addVersionIRI(object);
            consumer.consumeTriple(subject, predicate, object);
        }
    }
    public boolean canHandleStreaming(Identifier subject,Identifier predicate,Identifier object) {
        // Always apply at the end
        return false;
    }
    public boolean canHandle(Identifier subject, Identifier predicate, Identifier object) {
        return consumer.getOntologyIRIs().contains(subject);
    }
}
