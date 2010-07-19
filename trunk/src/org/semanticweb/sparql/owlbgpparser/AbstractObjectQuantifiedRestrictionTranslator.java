package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;

public abstract class AbstractObjectQuantifiedRestrictionTranslator extends AbstractObjectRestrictionTranslator {

    public AbstractObjectQuantifiedRestrictionTranslator(OWLRDFConsumer consumer) {
        super(consumer);
    }
    protected ClassExpression translateRestriction(Identifier mainNode) {
        Identifier fillerObject=getResourceObject(mainNode, getFillerTriplePredicate(), true);
        ObjectPropertyExpression prop=translateOnProperty(mainNode);
        ClassExpression desc=translateToClassExpression(fillerObject);
        return createRestriction(prop, desc);
    }
    protected abstract Identifier getFillerTriplePredicate();
    protected abstract ClassExpression createRestriction(ObjectPropertyExpression property,ClassExpression filler);
}
