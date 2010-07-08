package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.Clazz;
import org.semanticweb.sparql.owlbgp.model.ObjectExactCardinality;
import org.semanticweb.sparql.owlbgp.model.ObjectPropertyExpression;

public class ObjectCardinalityTranslator extends AbstractObjectCardinalityTranslator {

    public ObjectCardinalityTranslator(OWLRDFConsumer consumer) {
        super(consumer);
    }
    protected ClassExpression createRestriction(ObjectPropertyExpression prop, int cardi) {
        return ObjectExactCardinality.create(cardi, prop, Clazz.THING);
    }
    protected ClassExpression createRestriction(ObjectPropertyExpression prop, int cardi, ClassExpression filler) {
        return ObjectExactCardinality.create(cardi, prop, filler);
    }
    protected String getCardinalityTriplePredicate() {
        return Vocabulary.OWL_CARDINALITY.getIRI();
    }
    protected String getQualifiedCardinalityTriplePredicate() {
        return Vocabulary.OWL_QUALIFIED_CARDINALITY.getIRI();
    }
}
