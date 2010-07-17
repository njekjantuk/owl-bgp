package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.Declaration;
import org.semanticweb.sparql.owlbgp.model.Identifier;

public class TypeNamedIndividualHandler extends BuiltInTypeHandler {

    public TypeNamedIndividualHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_NAMED_INDIVIDUAL.getIRI());
    }

    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        if (!consumer.isAnonymousNode(subject)) {
            addAxiom(Declaration.create((Atomic)translateIndividual(subject), consumer.getPendingAnnotations()));
            consumer.addIndividual(subject);
            consumeTriple(subject, predicate, object);
        }
    }
}
