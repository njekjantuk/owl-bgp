package org.semanticweb.sparql.owlbgp.parser.translators;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ObjectComplementOf;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;

public class ComplementOfTranslator implements ClassExpressionTranslator {

    protected TripleConsumer consumer;
    
    public ComplementOfTranslator(TripleConsumer consumer) {
        this.consumer=consumer;
    }
    public ClassExpression translate(Identifier mainNode) {
        Identifier complementOfObject=consumer.getResourceObject(mainNode, Vocabulary.OWL_COMPLEMENT_OF.getIRI(), true);
        ClassExpression operand=consumer.translateClassExpression(complementOfObject);
        return ObjectComplementOf.create(operand);
    }
}
