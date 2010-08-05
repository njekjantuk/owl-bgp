package org.semanticweb.sparql.owlbgp.parser.translators;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;

public class ObjectPropertyExpressionListItemTranslator implements ListItemTranslator<ObjectPropertyExpression> {
    protected final TripleConsumer consumer;

    public ObjectPropertyExpressionListItemTranslator(TripleConsumer consumer) {
        this.consumer=consumer;
    }
    public ObjectPropertyExpression translate(Identifier iri) {
        return consumer.getObjectPropertyExpressionForObjectPropertyIdentifier(iri);
    }
}
