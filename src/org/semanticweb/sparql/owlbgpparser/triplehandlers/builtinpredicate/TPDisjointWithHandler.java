package org.semanticweb.sparql.owlbgpparser.triplehandlers.builtinpredicate;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.DisjointClasses;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgpparser.TripleConsumer;
import org.semanticweb.sparql.owlbgpparser.Vocabulary;
import org.semanticweb.sparql.owlbgpparser.triplehandlers.TriplePredicateHandler;

public class TPDisjointWithHandler extends TriplePredicateHandler {

    public TPDisjointWithHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_DISJOINT_WITH.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        // We can only handle disjoint axioms if we don't have to do
        // any translation of the subject or object - i.e. only if the
        // subject or object are named classes
        return !isSubjectOrObjectAnonymous(subject, object);
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        Set<ClassExpression> operands = new HashSet<ClassExpression>();
        operands.add(consumer.translateClassExpression(subject));
        operands.add(consumer.translateClassExpression(object));
        consumer.addAxiom(DisjointClasses.create(operands));
        consumer.consumeTriple(subject, predicate, object);
    }
}
