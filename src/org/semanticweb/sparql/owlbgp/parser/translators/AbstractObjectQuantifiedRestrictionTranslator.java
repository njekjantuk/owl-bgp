package org.semanticweb.sparql.owlbgp.parser.translators;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;

public abstract class AbstractObjectQuantifiedRestrictionTranslator extends AbstractObjectRestrictionTranslator {

    public AbstractObjectQuantifiedRestrictionTranslator(TripleConsumer consumer) {
        super(consumer);
    }
    protected ClassExpression translateRestriction(Identifier mainNode) {
        Identifier fillerObject=consumer.getResourceObject(mainNode, getFillerTriplePredicate(), true);
        ObjectPropertyExpression prop=translateOnProperty(mainNode);
        ClassExpression desc=consumer.translateClassExpression(fillerObject);
        return createRestriction(prop, desc);
    }
    protected abstract Identifier getFillerTriplePredicate();
    protected abstract ClassExpression createRestriction(ObjectPropertyExpression property,ClassExpression filler);
}
