package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.SubDataPropertyOf;

public class TPSubDataPropertyOfHandler extends TriplePredicateHandler {

    public TPSubDataPropertyOfHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_SUB_DATA_PROPERTY_OF.getIRI());
    }

    public boolean canHandleStreaming(String subject, String predicate, String object) {
        return true;
    }
    public void handleTriple(String subject, String predicate, String object) {
        addAxiom(SubDataPropertyOf.create(translateDataProperty(subject),translateDataProperty(object)));
        consumeTriple(subject, predicate, object);
    }
}
