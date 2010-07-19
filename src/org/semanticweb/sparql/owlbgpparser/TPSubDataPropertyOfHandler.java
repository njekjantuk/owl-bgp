package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.SubDataPropertyOf;

public class TPSubDataPropertyOfHandler extends TriplePredicateHandler {

    public TPSubDataPropertyOfHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_SUB_DATA_PROPERTY_OF.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        return true;
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        addAxiom(SubDataPropertyOf.create(translateDataProperty(subject),translateDataProperty(object)));
        consumeTriple(subject, predicate, object);
    }
}
