package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.ILiteral;
import org.semanticweb.sparql.owlbgp.model.ObjectPropertyExpression;

public class ObjectPropertyExpressionListItemTranslator implements ListItemTranslator<ObjectPropertyExpression> {
    protected final OWLRDFConsumer consumer;

    public ObjectPropertyExpressionListItemTranslator(OWLRDFConsumer consumer) {
        this.consumer = consumer;
    }
    public ObjectPropertyExpression translate(String iri) {
        return consumer.translateObjectPropertyExpression(iri);
    }
    public ObjectPropertyExpression translate(ILiteral firstObject) {
        throw new IllegalArgumentException("Cannot translate list item as an object property, because rdf:first triple is a literal triple");
    }
}
