package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.ObjectComplementOf;

public class ComplementOfTranslator extends AbstractClassExpressionTranslator {

    public ComplementOfTranslator(OWLRDFConsumer consumer) {
        super(consumer);
    }
    public ClassExpression translate(String mainNode) {
        String complementOfObject = getResourceObject(mainNode, Vocabulary.OWL_COMPLEMENT_OF.getIRI(), true);
        ClassExpression operand = translateToClassExpression(complementOfObject);
        return ObjectComplementOf.create(operand);
    }
}
