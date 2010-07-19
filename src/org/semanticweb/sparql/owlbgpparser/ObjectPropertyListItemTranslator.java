package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.literals.Literal;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;

public class ObjectPropertyListItemTranslator implements ListItemTranslator<ObjectPropertyExpression> {
    protected final OWLRDFConsumer consumer;

    public ObjectPropertyListItemTranslator(OWLRDFConsumer consumer) {
        this.consumer = consumer;
    }
    public ObjectPropertyExpression translate(Identifier firstObject) {
        return consumer.translateObjectPropertyExpression(firstObject);
    }
    public ObjectPropertyExpression translate(Literal firstObject) {
        throw new IllegalArgumentException("Cannot translate list item as an object property, because rdf:first triple is a literal triple");
    }
}
