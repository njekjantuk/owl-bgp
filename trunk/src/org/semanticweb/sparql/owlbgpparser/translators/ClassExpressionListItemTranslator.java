package org.semanticweb.sparql.owlbgpparser.translators;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.classexpressions.Clazz;
import org.semanticweb.sparql.owlbgp.model.literals.Literal;
import org.semanticweb.sparql.owlbgpparser.TripleConsumer;

public class ClassExpressionListItemTranslator implements ListItemTranslator<ClassExpression> {

    protected final TripleConsumer consumer;

    public ClassExpressionListItemTranslator(TripleConsumer consumer) {
        this.consumer = consumer;
    }
    public ClassExpression translate(Identifier iri) {
        return consumer.translateClassExpression(iri);
    }
    public ClassExpression translate(Literal firstObject) {
        return Clazz.THING;
    }
}
