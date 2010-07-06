package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.ObjectUnionOf;

public class TPUnionOfHandler extends AbstractNamedEquivalentClassAxiomHandler {

    public TPUnionOfHandler(OWLRDFConsumer consumer) {
        super(consumer, OWLRDFVocabulary.OWL_UNION_OF.getIRI());
    }
    
    protected ClassExpression translateEquivalentClass(String mainNode) {
        return ObjectUnionOf.create(consumer.translateToClassExpressionSet(mainNode));
    }
}
