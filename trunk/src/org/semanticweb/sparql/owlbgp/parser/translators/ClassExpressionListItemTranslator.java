package org.semanticweb.sparql.owlbgp.parser.translators;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;

public class ClassExpressionListItemTranslator implements ListItemTranslator<ClassExpression> {

    protected final TripleConsumer consumer;

    public ClassExpressionListItemTranslator(TripleConsumer consumer) {
        this.consumer = consumer;
    }
    public ClassExpression translate(Identifier iri) {
        consumer.translateClassExpression(iri);
        return consumer.getCE(iri);
    }
}
