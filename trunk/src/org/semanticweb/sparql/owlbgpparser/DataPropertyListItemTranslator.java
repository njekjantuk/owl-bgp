package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.DataPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.ILiteral;

public class DataPropertyListItemTranslator implements ListItemTranslator<DataPropertyExpression> {

    protected final OWLRDFConsumer consumer;

    public DataPropertyListItemTranslator(OWLRDFConsumer consumer) {
        this.consumer = consumer;
    }
    public DataPropertyExpression translate(String firstObject) {
        return consumer.translateDataPropertyExpression(firstObject);
    }
    public DataPropertyExpression translate(ILiteral firstObject) {
        throw new IllegalArgumentException("Cannot translate list item as an object property, because rdf:first triple is a literal triple");
    }
}
