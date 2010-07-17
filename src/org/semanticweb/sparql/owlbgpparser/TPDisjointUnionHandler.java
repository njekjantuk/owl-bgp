package org.semanticweb.sparql.owlbgpparser;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.ClassVariable;
import org.semanticweb.sparql.owlbgp.model.Clazz;
import org.semanticweb.sparql.owlbgp.model.DisjointUnion;
import org.semanticweb.sparql.owlbgp.model.Identifier;

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
