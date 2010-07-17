package org.semanticweb.sparql.owlbgpparser;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.Identifier;

public abstract class AbstractNaryBooleanClassExpressionTranslator extends AbstractClassExpressionTranslator {
    public AbstractNaryBooleanClassExpressionTranslator(OWLRDFConsumer consumer) {
        super(consumer);
    }
    public ClassExpression translate(Identifier mainNode) {
        Identifier object=getResourceObject(mainNode, getPredicateIRI(), true);
        Set<ClassExpression> operands=translateToClassExpressionSet(object);
        if (operands.size() < 2) {
            if (operands.size() == 1) return operands.iterator().next();
            else throw new IllegalArgumentException("An intersection didn't have any conjuncts. Main node: "+mainNode);
        }
        return createClassExpression(operands);
    }
    protected abstract ClassExpression createClassExpression(Set<ClassExpression> operands);
    protected abstract Identifier getPredicateIRI();
}
