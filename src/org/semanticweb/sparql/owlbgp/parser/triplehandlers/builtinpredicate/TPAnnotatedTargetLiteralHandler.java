package org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.literals.Literal;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.AbstractLiteralTripleHandler;

public class TPAnnotatedTargetLiteralHandler extends AbstractLiteralTripleHandler {

    public TPAnnotatedTargetLiteralHandler(TripleConsumer consumer) {
        super(consumer);
    }

    public boolean canHandle(Identifier subject,Identifier predicate,Literal object) {
        return predicate==(Identifier)Vocabulary.OWL_ANNOTATED_TARGET.getIRI();
    }
    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Literal object) {
        return canHandle(subject, predicate, object);
    }
    public void handleTriple(Identifier subject, Identifier predicate, Literal object) {
        consumer.addAnnoTarget2annoMainNode(object, subject);
        consumer.consumeTriple(subject, predicate, object);
    }
}
