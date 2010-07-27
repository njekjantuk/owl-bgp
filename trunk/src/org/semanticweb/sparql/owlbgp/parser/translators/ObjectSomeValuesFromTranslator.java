package org.semanticweb.sparql.owlbgp.parser.translators;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ObjectSomeValuesFrom;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;

public class ObjectSomeValuesFromTranslator extends AbstractObjectQuantifiedRestrictionTranslator {

    public ObjectSomeValuesFromTranslator(TripleConsumer consumer) {
        super(consumer);
    }
    protected ClassExpression createRestriction(ObjectPropertyExpression property,ClassExpression filler) {
        return ObjectSomeValuesFrom.create(property, filler);
    }
    protected Identifier getFillerTriplePredicate() {
        return Vocabulary.OWL_SOME_VALUES_FROM.getIRI();
    }
}
