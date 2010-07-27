package org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.DisjointUnion;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.TriplePredicateHandler;

public class TPDisjointUnionHandler extends TriplePredicateHandler {

    public TPDisjointUnionHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_DISJOINT_UNION_OF.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        // The list might contain anonymous classes - better not handle it
        // whilst streaming triples!
        return false;
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        ClassExpression cls=consumer.translateClassExpression(subject);
        Set<ClassExpression> classExpressions=consumer.translateToClassExpressionSet(object);
        consumer.addAxiom(DisjointUnion.create(cls, classExpressions));
        consumer.consumeTriple(subject, predicate, object);
    }
}
