package org.semanticweb.sparql.owlbgpparser.triplehandlers.builtinpredicate;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.DifferentIndividuals;
import org.semanticweb.sparql.owlbgp.model.individuals.Individual;
import org.semanticweb.sparql.owlbgpparser.TripleConsumer;
import org.semanticweb.sparql.owlbgpparser.Vocabulary;
import org.semanticweb.sparql.owlbgpparser.triplehandlers.TriplePredicateHandler;

public class TPDistinctMembersHandler extends TriplePredicateHandler {

    public TPDistinctMembersHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_DISTINCT_MEMBERS.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        // We need all of the list triples to be loaded :(
        return false;
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        Set<Individual> inds=consumer.translateToIndividualSet(object);
        consumer.addAxiom(DifferentIndividuals.create(inds));
        consumer.consumeTriple(subject, predicate, object);
    }
}
