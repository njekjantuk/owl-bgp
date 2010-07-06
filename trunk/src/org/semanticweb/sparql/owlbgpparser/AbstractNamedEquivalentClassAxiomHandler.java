package org.semanticweb.sparql.owlbgpparser;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.EquivalentClasses;

public abstract class AbstractNamedEquivalentClassAxiomHandler extends TriplePredicateHandler {

    public AbstractNamedEquivalentClassAxiomHandler(OWLRDFConsumer consumer, String predicateIRI) {
        super(consumer, predicateIRI);
    }

    public boolean canHandleStreaming(String subject, String predicate, String object) {
        return false;
    }
    public boolean canHandle(String subject, String predicate, String object) {
        return super.canHandle(subject, predicate, object) && !consumer.isAnonymousNode(subject);
    }
    public void handleTriple(String subject, String predicate, String object) {
        consumeTriple(subject, predicate, object);
        Set<ClassExpression> operands = new HashSet<ClassExpression>();
        operands.add(translateClassExpression(subject));
        operands.add(translateEquivalentClass(object));
        addAxiom(EquivalentClasses.create(operands));
    }

    protected abstract ClassExpression translateEquivalentClass(String mainNode);

}
