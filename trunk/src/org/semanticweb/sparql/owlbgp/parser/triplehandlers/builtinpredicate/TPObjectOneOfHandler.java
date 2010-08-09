package org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ObjectOneOf;
import org.semanticweb.sparql.owlbgp.model.individuals.Individual;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.TripleHandler;

public class TPObjectOneOfHandler extends TripleHandler {

    public TPObjectOneOfHandler(TripleConsumer consumer) {
        super(consumer);
    }

    @Override
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        Set<Individual> individualSet=consumer.translateToIndividualSet(object);
        if (individualSet!=null&&individualSet.size()>0)
            consumer.mapClassIdentifierToClassExpression(subject, ObjectOneOf.create(individualSet));
        else {
            // TODO: error handling
            System.err.println("error");
        }
    }
}
