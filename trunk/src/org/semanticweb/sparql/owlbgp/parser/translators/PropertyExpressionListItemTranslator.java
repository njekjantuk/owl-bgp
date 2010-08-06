package org.semanticweb.sparql.owlbgp.parser.translators;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.properties.PropertyExpression;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;

public class PropertyExpressionListItemTranslator implements ListItemTranslator<PropertyExpression> {
    protected final TripleConsumer consumer;

    public PropertyExpressionListItemTranslator(TripleConsumer consumer) {
        this.consumer=consumer;
    }
    public PropertyExpression translate(Identifier iri) {
        PropertyExpression pe=consumer.getObjectPropertyExpressionForObjectPropertyIdentifier(iri);
        if (pe==null) 
            pe=consumer.getDataPropertyExpressionForDataPropertyIdentifier(iri);
        return pe;
    }
}
