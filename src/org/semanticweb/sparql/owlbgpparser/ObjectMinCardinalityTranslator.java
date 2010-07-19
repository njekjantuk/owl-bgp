package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.classexpressions.Clazz;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ObjectMinCardinality;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;

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
    protected Identifier getCardinalityTriplePredicate() {
        return Vocabulary.OWL_MIN_CARDINALITY.getIRI();
    }
    protected Identifier getQualifiedCardinalityTriplePredicate() {
        return Vocabulary.OWL_MIN_QUALIFIED_CARDINALITY.getIRI();
    }
}
