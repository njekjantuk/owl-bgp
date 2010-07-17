package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.Declaration;
import org.semanticweb.sparql.owlbgp.model.Identifier;

public class TypeClassHandler extends BuiltInTypeHandler {

    public TypeClassHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_CLASS.getIRI());
    }

    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        if (!consumer.isAnonymousNode(subject)) {
            consumer.addAxiom(Declaration.create((Atomic)translateClassExpression(subject), consumer.getPendingAnnotations()));
            consumer.addClass(subject);
            consumeTriple(subject, predicate, object);
        }
    }
}
