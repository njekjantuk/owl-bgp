package org.semanticweb.sparql.owlbgpparser.translators;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.individuals.Individual;
import org.semanticweb.sparql.owlbgp.model.literals.Literal;
import org.semanticweb.sparql.owlbgpparser.TripleConsumer;

public class IndividualListItemTranslator implements ListItemTranslator<Individual> {

    protected final TripleConsumer consumer;

    public IndividualListItemTranslator(TripleConsumer consumer) {
        this.consumer=consumer;
    }
    public Individual translate(Identifier iri) {
        return consumer.translateIndividual(iri);
    }
    public Individual translate(Literal firstObject) {
        return null;
    }
}
