package org.semanticweb.sparql.owlbgpparser;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.ClassExpression;

public abstract class AbstractNaryBooleanClassExpressionTranslator extends AbstractClassExpressionTranslator {
    public AbstractNaryBooleanClassExpressionTranslator(OWLRDFConsumer consumer) {
        super(consumer);
    }
    public ClassExpression translate(String mainNode) {
        String object=getResourceObject(mainNode, getPredicateIRI(), true);
        Set<ClassExpression> operands=translateToClassExpressionSet(object);
        if (operands.size() < 2) {
            if(operands.size() == 1) {
                return operands.iterator().next();
            }
            else {
                throw new IllegalArgumentException("An intersection didn't have any conjuncts. Main node: "+mainNode);
            }
        }
        return createClassExpression(operands);
    }
    protected abstract ClassExpression createClassExpression(Set<ClassExpression> operands);
    protected abstract String getPredicateIRI();
}
