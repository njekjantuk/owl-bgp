package org.semanticweb.sparql.owlbgpparser;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.DisjointClasses;

public class TPDisjointWithHandler extends TriplePredicateHandler {

    public TPDisjointWithHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_DISJOINT_WITH.getIRI());
    }

    public boolean canHandleStreaming(String subject, String predicate, String object) {
        // We can only handle disjoint axioms if we don't have to do
        // any translation of the subject or object - i.e. only if the
        // subject or object are named classes
        return !isSubjectOrObjectAnonymous(subject, object);
    }
    public void handleTriple(String subject, String predicate, String object) {
        Set<ClassExpression> operands = new HashSet<ClassExpression>();
        operands.add(translateClassExpression(subject));
        operands.add(translateClassExpression(object));
        addAxiom(DisjointClasses.create(operands));
        consumeTriple(subject, predicate, object);
    }
}
