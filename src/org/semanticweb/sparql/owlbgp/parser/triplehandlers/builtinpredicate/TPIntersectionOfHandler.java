package org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ObjectIntersectionOf;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;

public class TPIntersectionOfHandler extends AbstractNamedEquivalentClassAxiomHandler {

    public TPIntersectionOfHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_INTERSECTION_OF.getIRI());
    }

    protected ClassExpression translateClass(Identifier mainNode) {
        return ObjectIntersectionOf.create(consumer.translateToClassExpressionSet(mainNode));
    }
}
