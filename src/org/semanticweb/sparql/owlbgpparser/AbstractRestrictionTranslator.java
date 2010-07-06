package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.ClassExpression;

public abstract class AbstractRestrictionTranslator extends AbstractClassExpressionTranslator {
    public AbstractRestrictionTranslator(OWLRDFConsumer consumer) {
        super(consumer);
    }
    public ClassExpression translate(String mainNode) {
        return translateRestriction(mainNode);
    }
    protected abstract ClassExpression translateRestriction(String mainNode);
}
