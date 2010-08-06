package org.semanticweb.sparql.owlbgp.parser.translators;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.properties.DataPropertyExpression;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;

public class DataPropertyExpressionListItemTranslator implements ListItemTranslator<DataPropertyExpression> {
    protected final TripleConsumer consumer;

    public DataPropertyExpressionListItemTranslator(TripleConsumer consumer) {
        this.consumer=consumer;
    }
    public DataPropertyExpression translate(Identifier iri) {
        return consumer.getDataPropertyExpressionForDataPropertyIdentifier(iri);
    }
}
