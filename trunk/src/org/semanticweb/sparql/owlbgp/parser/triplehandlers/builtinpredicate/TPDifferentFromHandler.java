package org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Annotation;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.DifferentIndividuals;
import org.semanticweb.sparql.owlbgp.model.individuals.Individual;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.TriplePredicateHandler;

public class TPDifferentFromHandler extends TriplePredicateHandler {

    public TPDifferentFromHandler(TripleConsumer consumer) {
        super(consumer,Vocabulary.OWL_DIFFERENT_FROM.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        return false;
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        Set<Individual> inds=new HashSet<Individual>();
        inds.add(consumer.translateIndividual(subject));
        inds.add(consumer.translateIndividual(object));
        Set<Annotation> annos=consumer.getAnnotations(subject, predicate, object, (Identifier)Vocabulary.OWL_AXIOM.getIRI());
        consumer.addAxiom(DifferentIndividuals.create(inds,annos));
        consumer.consumeTriple(subject, predicate, object);
    }
}
