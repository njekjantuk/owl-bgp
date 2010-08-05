package org.semanticweb.sparql.owlbgp.parser.translators;

import org.semanticweb.sparql.owlbgp.model.IRI;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.individuals.AnonymousIndividual;
import org.semanticweb.sparql.owlbgp.model.individuals.Individual;
import org.semanticweb.sparql.owlbgp.model.individuals.IndividualVariable;
import org.semanticweb.sparql.owlbgp.model.individuals.NamedIndividual;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;

public class IndividualListItemTranslator implements ListItemTranslator<Individual> {

    protected final TripleConsumer consumer;

    public IndividualListItemTranslator(TripleConsumer consumer) {
        this.consumer=consumer;
    }
    
    public Individual translate(Identifier firstObject) {
        if (firstObject instanceof IRI) 
            return NamedIndividual.create((IRI)firstObject);
        else if (consumer.isAnonymous(firstObject))
            return AnonymousIndividual.create(firstObject.toString());
        else if (consumer.isVariable(firstObject))
            return IndividualVariable.create(firstObject.toString());
        else
            throw new IllegalArgumentException("Cannot translate list item to an individual because the object of the rdf:first triple is not an individual. ");
    }
}
