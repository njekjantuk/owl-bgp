package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.ClassVariable;
import org.semanticweb.sparql.owlbgp.model.Clazz;

public class NamedClassTranslator implements ClassExpressionTranslator {

    protected final OWLRDFConsumer consumer;

    public NamedClassTranslator(OWLRDFConsumer consumer) {
        this.consumer=consumer;
    }
    public ClassExpression translate(String mainNode) {
        if (consumer.isVariableNode(mainNode)) return ClassVariable.create(mainNode);
    	return Clazz.create(mainNode); 
    }
}
