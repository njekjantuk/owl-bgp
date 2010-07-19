package org.semanticweb.sparql.owlbgpparser;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ObjectOneOf;
import org.semanticweb.sparql.owlbgp.model.individuals.Individual;

public class OneOfTranslator extends AbstractClassExpressionTranslator {

    public OneOfTranslator(OWLRDFConsumer consumer) {
        super(consumer);
    }
    public ClassExpression translate(Identifier mainNode) {
        Identifier oneOfObject=getResourceObject(mainNode, Vocabulary.OWL_ONE_OF.getIRI(), true);
        Set<Individual> individuals=translateToIndividualSet(oneOfObject);
        for (Individual ind : individuals) 
            consumer.addIndividual(ind.getIdentifier());
        if (individuals.isEmpty()) throw new IllegalArgumentException("Empty set in owl:oneOf class expression for main node: "+mainNode);
        return ObjectOneOf.create(individuals);
    }
}
