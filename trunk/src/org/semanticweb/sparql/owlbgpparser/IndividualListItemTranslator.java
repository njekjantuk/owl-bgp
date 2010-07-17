package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.ILiteral;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.Individual;

public class IndividualListItemTranslator implements ListItemTranslator<Individual> {

    protected final OWLRDFConsumer consumer;

    public IndividualListItemTranslator(OWLRDFConsumer consumer) {
        this.consumer=consumer;
    }
    public Individual translate(Identifier iri) {
        return consumer.translateIndividual(iri);
    }
    public Individual translate(ILiteral firstObject) {
        return null;
    }
}
