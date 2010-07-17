package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.Identifier;

public class NamedClassTranslator implements ClassExpressionTranslator {

    protected final OWLRDFConsumer consumer;

    public NamedClassTranslator(OWLRDFConsumer consumer) {
        this.consumer=consumer;
    }
    public ClassExpression translate(Identifier mainNode) {
        return consumer.translateClassExpression(mainNode);
    }
}
