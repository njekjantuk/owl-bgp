package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ObjectAllValuesFrom;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;

public class ObjectAllValuesFromTranslator extends AbstractObjectQuantifiedRestrictionTranslator {
    public ObjectAllValuesFromTranslator(OWLRDFConsumer consumer) {
        super(consumer);
    }
    protected ClassExpression createRestriction(ObjectPropertyExpression property,ClassExpression filler) {
        return ObjectAllValuesFrom.create(property, filler);
    }
    protected Identifier getFillerTriplePredicate() {
        return Vocabulary.OWL_ALL_VALUES_FROM.getIRI();
    }
}
