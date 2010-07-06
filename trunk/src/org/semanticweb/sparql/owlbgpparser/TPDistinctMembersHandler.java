package org.semanticweb.sparql.owlbgpparser;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.DifferentIndividuals;
import org.semanticweb.sparql.owlbgp.model.Individual;

public class TPDistinctMembersHandler extends TriplePredicateHandler {

    public TPDistinctMembersHandler(OWLRDFConsumer consumer) {
        super(consumer, OWLRDFVocabulary.OWL_DISTINCT_MEMBERS.getIRI());
    }

    public boolean canHandleStreaming(String subject, String predicate, String object) {
        // We need all of the list triples to be loaded :(
        return false;
    }
    public void handleTriple(String subject, String predicate, String object) {
        Set<Individual> inds = getConsumer().translateToIndividualSet(object);
        addAxiom(DifferentIndividuals.create(inds));
        consumeTriple(subject, predicate, object);
    }
}
