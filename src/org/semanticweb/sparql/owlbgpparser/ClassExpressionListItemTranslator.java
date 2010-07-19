package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.classexpressions.Clazz;
import org.semanticweb.sparql.owlbgp.model.literals.Literal;

public class ClassExpressionListItemTranslator implements ListItemTranslator<ClassExpression> {

    protected final OWLRDFConsumer consumer;

    public ClassExpressionListItemTranslator(OWLRDFConsumer consumer) {
        this.consumer = consumer;
    }
    public ClassExpression translate(Identifier iri) {
        return consumer.translateClassExpression(iri);
    }
    public ClassExpression translate(Literal firstObject) {
        return Clazz.THING;
    }
}
