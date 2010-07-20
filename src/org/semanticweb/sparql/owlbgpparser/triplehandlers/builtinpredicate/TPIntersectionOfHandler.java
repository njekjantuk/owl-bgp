package org.semanticweb.sparql.owlbgpparser.triplehandlers.builtinpredicate;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ObjectIntersectionOf;
import org.semanticweb.sparql.owlbgpparser.TripleConsumer;
import org.semanticweb.sparql.owlbgpparser.Vocabulary;

public class TPIntersectionOfHandler extends AbstractNamedEquivalentClassAxiomHandler {

    public TPIntersectionOfHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_INTERSECTION_OF.getIRI());
    }

    protected ClassExpression translateEquivalentClass(Identifier mainNode) {
        return ObjectIntersectionOf.create(consumer.translateToClassExpressionSet(mainNode));
    }
}
