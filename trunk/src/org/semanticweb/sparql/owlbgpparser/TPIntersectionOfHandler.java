package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.ObjectIntersectionOf;

public class TPIntersectionOfHandler extends AbstractNamedEquivalentClassAxiomHandler {

    public TPIntersectionOfHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_INTERSECTION_OF.getIRI());
    }

    protected ClassExpression translateEquivalentClass(String mainNode) {
        return ObjectIntersectionOf.create(consumer.translateToClassExpressionSet(mainNode));
    }
}
