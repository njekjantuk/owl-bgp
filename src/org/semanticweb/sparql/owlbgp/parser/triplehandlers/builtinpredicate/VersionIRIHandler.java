package org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.TriplePredicateHandler;

public class VersionIRIHandler extends TriplePredicateHandler {

    public VersionIRIHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_VERSION_IRI);
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
