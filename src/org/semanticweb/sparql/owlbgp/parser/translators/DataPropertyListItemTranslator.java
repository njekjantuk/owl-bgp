package org.semanticweb.sparql.owlbgp.parser.translators;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.literals.Literal;
import org.semanticweb.sparql.owlbgp.model.properties.DataPropertyExpression;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;

public class DataPropertyListItemTranslator implements ListItemTranslator<DataPropertyExpression> {

    protected final TripleConsumer consumer;

    public DataPropertyListItemTranslator(TripleConsumer consumer) {
        this.consumer = consumer;
    }
    public DataPropertyExpression translate(Identifier firstObject) {
        return consumer.translateDataPropertyExpression(firstObject);
    }
    public DataPropertyExpression translate(Literal firstObject) {
        throw new IllegalArgumentException("Cannot translate list item as an object property, because rdf:first triple is a literal triple");
    }
}
