package org.semanticweb.sparql.owlbgpparser;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.DisjointUnion;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassVariable;
import org.semanticweb.sparql.owlbgp.model.classexpressions.Clazz;

public class TPDisjointUnionHandler extends TriplePredicateHandler {

    public TPDisjointUnionHandler(OWLRDFConsumer consumer) {
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
        if (cls instanceof ClassVariable) addAxiom(DisjointUnion.create((ClassVariable)cls, classExpressions));
        else addAxiom(DisjointUnion.create((Clazz)cls, classExpressions));
        consumeTriple(subject, predicate, object);
    }
}
