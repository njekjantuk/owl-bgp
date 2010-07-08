package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.Clazz;
import org.semanticweb.sparql.owlbgp.model.ObjectMinCardinality;
import org.semanticweb.sparql.owlbgp.model.ObjectPropertyExpression;

public class ObjectMinCardinalityTranslator extends AbstractObjectCardinalityTranslator {

    public ObjectMinCardinalityTranslator(OWLRDFConsumer consumer) {
        super(consumer);
    }
    protected ClassExpression createRestriction(ObjectPropertyExpression prop,int cardi) {
        return ObjectMinCardinality.create(cardi,prop,Clazz.THING);
    }
    protected ClassExpression createRestriction(ObjectPropertyExpression prop,int cardi,ClassExpression filler) {
        return ObjectMinCardinality.create(cardi, prop, filler);
    }
    protected String getCardinalityTriplePredicate() {
        return Vocabulary.OWL_MIN_CARDINALITY.getIRI();
    }
    protected String getQualifiedCardinalityTriplePredicate() {
        return Vocabulary.OWL_MIN_QUALIFIED_CARDINALITY.getIRI();
    }
}
