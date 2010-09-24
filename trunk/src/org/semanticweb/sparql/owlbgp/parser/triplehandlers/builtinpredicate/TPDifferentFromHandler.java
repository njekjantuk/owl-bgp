package org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Annotation;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.DifferentIndividuals;
import org.semanticweb.sparql.owlbgp.model.individuals.Individual;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.TripleHandler;

public class TPDifferentFromHandler extends TripleHandler {

    public TPDifferentFromHandler(TripleConsumer consumer) {
        super(consumer);
    }

    @Override
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object, Set<Annotation> annotations) {
        Individual ind1=consumer.getIND(subject);
        Individual ind2=consumer.getIND(object);
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
