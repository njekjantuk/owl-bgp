package org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.DisjointClasses;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;

public class TypeAllDisjointClassesHandler extends BuiltInTypeHandler {

    public TypeAllDisjointClassesHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_ALL_DISJOINT_CLASSES.getIRI());
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        consumer.consumeTriple(subject, predicate, object);
        Identifier listNode=consumer.getResourceObject(subject, Vocabulary.OWL_MEMBERS.getIRI(), true);
        if (listNode!=null)
            consumer.addAxiom(DisjointClasses.create(consumer.translateToClassExpressionSet(listNode),consumer.getAnnotations(subject)));
    }
    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        return false;
    }
}
