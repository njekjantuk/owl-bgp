package org.semanticweb.sparql.owlbgpparser.translators;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.classexpressions.Clazz;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ObjectExactCardinality;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;
import org.semanticweb.sparql.owlbgpparser.TripleConsumer;
import org.semanticweb.sparql.owlbgpparser.Vocabulary;

public class ObjectCardinalityTranslator extends AbstractObjectCardinalityTranslator {

    public ObjectCardinalityTranslator(TripleConsumer consumer) {
        super(consumer);
    }
    protected ClassExpression createRestriction(ObjectPropertyExpression prop, int cardi) {
        return ObjectExactCardinality.create(cardi, prop, Clazz.THING);
    }
    protected ClassExpression createRestriction(ObjectPropertyExpression prop, int cardi, ClassExpression filler) {
        return ObjectExactCardinality.create(cardi, prop, filler);
    }
    protected Identifier getCardinalityTriplePredicate() {
        return Vocabulary.OWL_CARDINALITY.getIRI();
    }
    protected Identifier getQualifiedCardinalityTriplePredicate() {
        return Vocabulary.OWL_QUALIFIED_CARDINALITY.getIRI();
    }
}
