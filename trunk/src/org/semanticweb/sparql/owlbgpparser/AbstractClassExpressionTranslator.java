package org.semanticweb.sparql.owlbgpparser;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.ILiteral;
import org.semanticweb.sparql.owlbgp.model.Individual;

public abstract class AbstractClassExpressionTranslator implements ClassExpressionTranslator {
    protected final OWLRDFConsumer consumer;

    protected AbstractClassExpressionTranslator(OWLRDFConsumer consumer) {
        this.consumer=consumer;
    }
    public OWLRDFConsumer getConsumer() {
        return consumer;
    }
    protected String getResourceObject(String subject, String predicate, boolean consume) {
        return consumer.getResourceObject(subject, predicate, consume);
    }
    protected ILiteral getLiteralObject(String subject, String predicate, boolean consume) {
        return consumer.getLiteralObject(subject, predicate, consume);
    }
    protected boolean isTriplePresent(String mainNode, String predicate, String value, boolean consume) {
        return consumer.isTriplePresent(mainNode, predicate, value, true);
    }
    protected Set<ClassExpression> translateToClassExpressionSet(String mainNode) {
        return consumer.translateToClassExpressionSet(mainNode);
    }
    protected Set<Individual> translateToIndividualSet(String mainNode) {
        return consumer.translateToIndividualSet(mainNode);
    }
    protected ClassExpression translateToClassExpression(String mainNode) {
        return consumer.translateClassExpression(mainNode);
    }
}
