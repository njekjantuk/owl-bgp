package org.semanticweb.sparql.owlbgp.parser.translators;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;

public abstract class AbstractNaryBooleanClassExpressionTranslator implements ClassExpressionTranslator {

    protected final TripleConsumer consumer;

    public AbstractNaryBooleanClassExpressionTranslator(TripleConsumer consumer) {
        this.consumer=consumer;
    }
    public ClassExpression translate(Identifier mainNode) {
        Identifier object=consumer.getResourceObject(mainNode, getPredicateIRI(), true);
        Set<ClassExpression> operands=consumer.translateToClassExpressionSet(object);
        if (operands.size() < 2) {
            if (operands.size() == 1) return operands.iterator().next();
            else throw new IllegalArgumentException("An intersection didn't have any conjuncts. Main node: "+mainNode);
        }
        return createClassExpression(operands);
    }
    protected abstract ClassExpression createClassExpression(Set<ClassExpression> operands);
    protected abstract Identifier getPredicateIRI();
}
