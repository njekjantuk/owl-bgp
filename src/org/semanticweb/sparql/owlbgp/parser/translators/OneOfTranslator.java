package org.semanticweb.sparql.owlbgp.parser.translators;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ObjectOneOf;
import org.semanticweb.sparql.owlbgp.model.individuals.Individual;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;

public class OneOfTranslator implements ClassExpressionTranslator {
    
    protected final TripleConsumer consumer;
    
    public OneOfTranslator(TripleConsumer consumer) {
        this.consumer=consumer;
    }
    public ClassExpression translate(Identifier mainNode) {
        Identifier oneOfObject=consumer.getResourceObject(mainNode, Vocabulary.OWL_ONE_OF.getIRI(), true);
        Set<Individual> individuals=consumer.translateToIndividualSet(oneOfObject);
        for (Individual ind : individuals) 
            consumer.addIndividual(ind.getIdentifier());
        if (individuals.isEmpty()) throw new IllegalArgumentException("Empty set in owl:oneOf class expression for main node: "+mainNode);
        return ObjectOneOf.create(individuals);
    }
}
