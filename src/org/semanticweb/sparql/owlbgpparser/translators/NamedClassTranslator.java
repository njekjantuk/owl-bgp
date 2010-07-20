package org.semanticweb.sparql.owlbgpparser.translators;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgpparser.TripleConsumer;

public class NamedClassTranslator implements ClassExpressionTranslator {

    protected final TripleConsumer consumer;

    public NamedClassTranslator(TripleConsumer consumer) {
        this.consumer=consumer;
    }
    public ClassExpression translate(Identifier mainNode) {
        return consumer.translateClassExpression(mainNode);
    }
}
