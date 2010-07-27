package org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.TriplePredicateHandler;

public class TPAnnotatedTargetHandler extends TriplePredicateHandler {

    public TPAnnotatedTargetHandler(TripleConsumer consumer) {
        super(consumer,Vocabulary.OWL_ANNOTATED_TARGET.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        return true;
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        consumer.addAnnoTarget2annoMainNode(object, subject);
        consumer.consumeTriple(subject, predicate, object);
    }
}
