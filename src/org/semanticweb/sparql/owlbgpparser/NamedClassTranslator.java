package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;

public class NamedClassTranslator implements ClassExpressionTranslator {

    protected final OWLRDFConsumer consumer;

    public NamedClassTranslator(OWLRDFConsumer consumer) {
        this.consumer=consumer;
    }
    public ClassExpression translate(Identifier mainNode) {
        return consumer.translateClassExpression(mainNode);
    }
}
