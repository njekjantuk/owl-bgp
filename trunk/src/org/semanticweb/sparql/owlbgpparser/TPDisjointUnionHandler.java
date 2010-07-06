package org.semanticweb.sparql.owlbgpparser;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.ClassVariable;
import org.semanticweb.sparql.owlbgp.model.Clazz;
import org.semanticweb.sparql.owlbgp.model.DisjointUnion;

public class TPDisjointUnionHandler extends TriplePredicateHandler {

    public TPDisjointUnionHandler(OWLRDFConsumer consumer) {
        super(consumer, OWLRDFVocabulary.OWL_DISJOINT_UNION_OF.getIRI());
    }

    public boolean canHandleStreaming(String subject, String predicate, String object) {
        // The list might contain anonymous classes - better not handle it
        // whilst streaming triples!
        return false;
    }
    public void handleTriple(String subject, String predicate, String object) {
        ClassExpression cls=consumer.translateClassExpression(subject);
        Set<ClassExpression> classExpressions=consumer.translateToClassExpressionSet(object);
        if (cls instanceof ClassVariable) addAxiom(DisjointUnion.create((ClassVariable)cls, classExpressions));
        else addAxiom(DisjointUnion.create((Clazz)cls, classExpressions));
        consumeTriple(subject, predicate, object);
    }
}
