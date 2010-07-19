package org.semanticweb.sparql.owlbgpparser;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.EquivalentClasses;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;

public abstract class AbstractNamedEquivalentClassAxiomHandler extends TriplePredicateHandler {

    public AbstractNamedEquivalentClassAxiomHandler(OWLRDFConsumer consumer, Identifier predicateIRI) {
        super(consumer, predicateIRI);
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        return false;
    }
    public boolean canHandle(Identifier subject, Identifier predicate, Identifier object) {
        return super.canHandle(subject, predicate, object) && !consumer.isAnonymousNode(subject);
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        consumeTriple(subject, predicate, object);
        Set<ClassExpression> operands = new HashSet<ClassExpression>();
        operands.add(translateClassExpression(subject));
        operands.add(translateEquivalentClass(object));
        addAxiom(EquivalentClasses.create(operands));
    }

    protected abstract ClassExpression translateEquivalentClass(Identifier mainNode);

}
