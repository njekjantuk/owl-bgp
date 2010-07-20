package org.semanticweb.sparql.owlbgpparser.triplehandlers.builtinpredicate;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.EquivalentClasses;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgpparser.TripleConsumer;
import org.semanticweb.sparql.owlbgpparser.triplehandlers.TriplePredicateHandler;

public abstract class AbstractNamedEquivalentClassAxiomHandler extends TriplePredicateHandler {

    public AbstractNamedEquivalentClassAxiomHandler(TripleConsumer consumer, Identifier predicateIRI) {
        super(consumer, predicateIRI);
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        return false;
    }
    public boolean canHandle(Identifier subject, Identifier predicate, Identifier object) {
        return super.canHandle(subject, predicate, object) && !consumer.isAnonymous(subject);
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        consumer.consumeTriple(subject, predicate, object);
        Set<ClassExpression> operands = new HashSet<ClassExpression>();
        operands.add(consumer.translateClassExpression(subject));
        operands.add(translateEquivalentClass(object));
        consumer.addAxiom(EquivalentClasses.create(operands));
    }

    protected abstract ClassExpression translateEquivalentClass(Identifier mainNode);

}
