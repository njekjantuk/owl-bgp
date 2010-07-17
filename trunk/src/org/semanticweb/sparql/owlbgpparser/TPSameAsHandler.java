package org.semanticweb.sparql.owlbgpparser;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.Individual;
import org.semanticweb.sparql.owlbgp.model.SameIndividual;

public class TPSameAsHandler extends TriplePredicateHandler {

    public TPSameAsHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_SAME_AS.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        return true;
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        Set<Individual> inds = new HashSet<Individual>();
        inds.add(translateIndividual(subject));
        inds.add(translateIndividual(object));
        addAxiom(SameIndividual.create(inds));
        consumeTriple(subject, predicate, object);
    }
}
