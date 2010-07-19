package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.individuals.Individual;
import org.semanticweb.sparql.owlbgp.model.literals.Literal;

public class IndividualListItemTranslator implements ListItemTranslator<Individual> {

    protected final OWLRDFConsumer consumer;

    public IndividualListItemTranslator(OWLRDFConsumer consumer) {
        this.consumer=consumer;
    }
    public Individual translate(Identifier iri) {
        return consumer.translateIndividual(iri);
    }
    public Individual translate(Literal firstObject) {
        return null;
    }
}
