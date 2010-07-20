package org.semanticweb.sparql.owlbgpparser.translators;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.literals.Literal;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;
import org.semanticweb.sparql.owlbgpparser.TripleConsumer;

public class ObjectPropertyListItemTranslator implements ListItemTranslator<ObjectPropertyExpression> {
    protected final TripleConsumer consumer;

    public ObjectPropertyListItemTranslator(TripleConsumer consumer) {
        this.consumer = consumer;
    }
    public ObjectPropertyExpression translate(Identifier firstObject) {
        return consumer.translateObjectPropertyExpression(firstObject);
    }
    public ObjectPropertyExpression translate(Literal firstObject) {
        throw new IllegalArgumentException("Cannot translate list item as an object property, because rdf:first triple is a literal triple");
    }
}
