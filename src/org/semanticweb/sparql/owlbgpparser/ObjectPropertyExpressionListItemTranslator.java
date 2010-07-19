package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.literals.Literal;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;

public class ObjectPropertyExpressionListItemTranslator implements ListItemTranslator<ObjectPropertyExpression> {
    protected final OWLRDFConsumer consumer;

    public ObjectPropertyExpressionListItemTranslator(OWLRDFConsumer consumer) {
        this.consumer = consumer;
    }
    public ObjectPropertyExpression translate(Identifier iri) {
        return consumer.translateObjectPropertyExpression(iri);
    }
    public ObjectPropertyExpression translate(Literal firstObject) {
        throw new IllegalArgumentException("Cannot translate list item as an object property, because rdf:first triple is a literal triple");
    }
}
