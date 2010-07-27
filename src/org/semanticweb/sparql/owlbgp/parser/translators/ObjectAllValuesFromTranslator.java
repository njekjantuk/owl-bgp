package org.semanticweb.sparql.owlbgp.parser.translators;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ObjectAllValuesFrom;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;

public class ObjectAllValuesFromTranslator extends AbstractObjectQuantifiedRestrictionTranslator {
    public ObjectAllValuesFromTranslator(TripleConsumer consumer) {
        super(consumer);
    }
    protected ClassExpression createRestriction(ObjectPropertyExpression property,ClassExpression filler) {
        return ObjectAllValuesFrom.create(property, filler);
    }
    protected Identifier getFillerTriplePredicate() {
        return Vocabulary.OWL_ALL_VALUES_FROM.getIRI();
    }
}
