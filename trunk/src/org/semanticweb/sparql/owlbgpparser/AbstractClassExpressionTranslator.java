package org.semanticweb.sparql.owlbgpparser;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.ILiteral;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.Individual;

public abstract class AbstractClassExpressionTranslator implements ClassExpressionTranslator {
    protected final OWLRDFConsumer consumer;

    protected AbstractClassExpressionTranslator(OWLRDFConsumer consumer) {
        this.consumer=consumer;
    }
    public OWLRDFConsumer getConsumer() {
        return consumer;
    }
    protected Identifier getResourceObject(Identifier subject, Identifier predicate, boolean consume) {
        return consumer.getResourceObject(subject, predicate, consume);
    }
    protected ILiteral getLiteralObject(Identifier subject, Identifier predicate, boolean consume) {
        return consumer.getLiteralObject(subject, predicate, consume);
    }
    protected boolean isTriplePresent(Identifier mainNode, Identifier predicate, Identifier value, boolean consume) {
        return consumer.isTriplePresent(mainNode, predicate, value, true);
    }
    protected Set<ClassExpression> translateToClassExpressionSet(Identifier mainNode) {
        return consumer.translateToClassExpressionSet(mainNode);
    }
    protected Set<Individual> translateToIndividualSet(Identifier mainNode) {
        return consumer.translateToIndividualSet(mainNode);
    }
    protected ClassExpression translateToClassExpression(Identifier mainNode) {
        return consumer.translateClassExpression(mainNode);
    }
}
