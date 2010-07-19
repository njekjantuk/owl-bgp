package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ObjectSomeValuesFrom;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;

public class ObjectSomeValuesFromTranslator extends AbstractObjectQuantifiedRestrictionTranslator {

    public ObjectSomeValuesFromTranslator(OWLRDFConsumer consumer) {
        super(consumer);
    }
    protected ClassExpression createRestriction(ObjectPropertyExpression property,ClassExpression filler) {
        return ObjectSomeValuesFrom.create(property, filler);
    }
    protected Identifier getFillerTriplePredicate() {
        return Vocabulary.OWL_SOME_VALUES_FROM.getIRI();
    }
}
