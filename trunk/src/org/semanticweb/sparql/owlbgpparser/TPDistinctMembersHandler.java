package org.semanticweb.sparql.owlbgpparser;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.DifferentIndividuals;
import org.semanticweb.sparql.owlbgp.model.individuals.Individual;

public class TPDistinctMembersHandler extends TriplePredicateHandler {

    public TPDistinctMembersHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_DISTINCT_MEMBERS.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        // We need all of the list triples to be loaded :(
        return false;
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        Set<Individual> inds = getConsumer().translateToIndividualSet(object);
        addAxiom(DifferentIndividuals.create(inds));
        consumeTriple(subject, predicate, object);
    }
}