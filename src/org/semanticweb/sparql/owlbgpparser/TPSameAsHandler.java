package org.semanticweb.sparql.owlbgpparser;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Individual;
import org.semanticweb.sparql.owlbgp.model.SameIndividual;

public class TPSameAsHandler extends TriplePredicateHandler {

    public TPSameAsHandler(OWLRDFConsumer consumer) {
        super(consumer, OWLRDFVocabulary.OWL_SAME_AS.getIRI());
    }

    public boolean canHandleStreaming(String subject, String predicate, String object) {
        return true;
    }
    public void handleTriple(String subject, String predicate, String object) {
        Set<Individual> inds = new HashSet<Individual>();
        inds.add(translateIndividual(subject));
        inds.add(translateIndividual(object));
        addAxiom(SameIndividual.create(inds));
        consumeTriple(subject, predicate, object);
    }
}
