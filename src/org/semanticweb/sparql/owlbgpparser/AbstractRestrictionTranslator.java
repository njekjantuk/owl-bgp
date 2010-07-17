package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.Identifier;

public abstract class AbstractRestrictionTranslator extends AbstractClassExpressionTranslator {
    public AbstractRestrictionTranslator(OWLRDFConsumer consumer) {
        super(consumer);
    }
    public ClassExpression translate(Identifier mainNode) {
        return translateRestriction(mainNode);
    }
    protected abstract ClassExpression translateRestriction(Identifier mainNode);
}
