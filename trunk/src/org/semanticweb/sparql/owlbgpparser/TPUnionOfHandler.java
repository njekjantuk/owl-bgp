package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ObjectUnionOf;

public class TPUnionOfHandler extends AbstractNamedEquivalentClassAxiomHandler {

    public TPUnionOfHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_UNION_OF.getIRI());
    }
    
    protected ClassExpression translateEquivalentClass(Identifier mainNode) {
        return ObjectUnionOf.create(consumer.translateToClassExpressionSet(mainNode));
    }
}
