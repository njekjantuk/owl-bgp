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
        super(consumer, Vocabulary.OWL_DIFFERENT_FROM);
    }

    @Override
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object, Set<Annotation> annotations) {
        Individual ind1=consumer.getIndividualForIndividualIdentifier(subject);
        Individual ind2=consumer.getIndividualForIndividualIdentifier(object);
        String errorMessage="";
        if (ind1==null)
            errorMessage="Could not find an individual for the subject in the triple "+subject+" "+predicate+" "+object+". ";
        if (ind2==null)
            errorMessage+="Could not find an individual for the object in the triple "+subject+" "+predicate+" "+object+". ";
        if (ind1!=null && ind2!=null) {
            Set<Individual> inds=new HashSet<Individual>();
            inds.add(ind1);
            inds.add(ind2);
            consumer.addAxiom(DifferentIndividuals.create(inds,annotations));
        } else {
            throw new RuntimeException(errorMessage);
        }
    }
}