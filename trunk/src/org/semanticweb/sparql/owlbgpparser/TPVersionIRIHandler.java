package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.IRI;
import org.semanticweb.sparql.owlbgp.model.Identifier;

public class TPVersionIRIHandler extends TriplePredicateHandler {

    public TPVersionIRIHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_VERSION_IRI.getIRI());
    }

    public void handleTriple(Identifier subject,Identifier predicate,Identifier object) {
        if (object instanceof IRI) {
            consumer.setVersionIRI((IRI)object);
            consumeTriple(subject, predicate, object);
        }
    }
    public boolean canHandleStreaming(Identifier subject,Identifier predicate,Identifier object) {
        // Always apply at the end
        return false;
    }
    public boolean canHandle(Identifier subject, Identifier predicate, Identifier object) {
        return subject.equals(consumer.ontologyIRI);
    }
}
