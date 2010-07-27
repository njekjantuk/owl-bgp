package org.semanticweb.sparql.owlbgp.parser.translators;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;

public class NamedClassTranslator implements ClassExpressionTranslator {

    protected final TripleConsumer consumer;

    public NamedClassTranslator(TripleConsumer consumer) {
        this.consumer=consumer;
    }
    public ClassExpression translate(Identifier mainNode) {
        return consumer.translateClassExpression(mainNode);
    }
}
