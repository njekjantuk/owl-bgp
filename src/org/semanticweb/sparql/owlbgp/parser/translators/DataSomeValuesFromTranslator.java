package org.semanticweb.sparql.owlbgp.parser.translators;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.classexpressions.DataSomeValuesFrom;
import org.semanticweb.sparql.owlbgp.model.dataranges.DataRange;
import org.semanticweb.sparql.owlbgp.model.properties.DataPropertyExpression;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;

public class DataSomeValuesFromTranslator extends AbstractDataQuantifiedRestrictionTranslator {

    public DataSomeValuesFromTranslator(TripleConsumer consumer) {
        super(consumer);
    }
    protected ClassExpression createRestriction(DataPropertyExpression prop, DataRange filler) {
        return DataSomeValuesFrom.create(prop, filler);
    }
    protected Identifier getFillerTriplePredicate() {
        return Vocabulary.OWL_SOME_VALUES_FROM.getIRI();
    }
}
