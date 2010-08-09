package org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.TripleHandler;

public class VersionIRIHandler extends TripleHandler {

    public VersionIRIHandler(TripleConsumer consumer) {
        super(consumer);
    }

    @Override
    public void handleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        super.handleStreaming(subject, predicate, object, true);
        if (consumer.isAnonymous(object)) {
            throw new RuntimeException("The object of a version IRI triple is anonymous/a blank node, which is not allowed: "+object);
        } else if (consumer.isAnonymous(subject)) {
            throw new RuntimeException("The subject of a version IRI triple is anonymous/a blank node, which is not allowed: "+subject);
        } else {
            consumer.addVersionIRI(object);
        }
    }
}
