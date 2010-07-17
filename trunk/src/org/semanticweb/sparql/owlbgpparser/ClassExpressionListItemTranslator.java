package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.Clazz;
import org.semanticweb.sparql.owlbgp.model.ILiteral;
import org.semanticweb.sparql.owlbgp.model.Identifier;

public class ClassExpressionListItemTranslator implements ListItemTranslator<ClassExpression> {

    protected final OWLRDFConsumer consumer;

    public ClassExpressionListItemTranslator(OWLRDFConsumer consumer) {
        this.consumer = consumer;
    }
    public ClassExpression translate(Identifier iri) {
        return consumer.translateClassExpression(iri);
    }
    public ClassExpression translate(ILiteral firstObject) {
        return Clazz.THING;
    }
}
