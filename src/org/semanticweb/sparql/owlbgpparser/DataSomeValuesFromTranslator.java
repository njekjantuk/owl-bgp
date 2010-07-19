package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.classexpressions.DataSomeValuesFrom;
import org.semanticweb.sparql.owlbgp.model.dataranges.DataRange;
import org.semanticweb.sparql.owlbgp.model.properties.DataPropertyExpression;

public class DataSomeValuesFromTranslator extends AbstractDataQuantifiedRestrictionTranslator {

    public DataSomeValuesFromTranslator(OWLRDFConsumer consumer) {
        super(consumer);
    }
    protected ClassExpression createRestriction(DataPropertyExpression prop, DataRange filler) {
        return DataSomeValuesFrom.create(prop, filler);
    }
    protected Identifier getFillerTriplePredicate() {
        return Vocabulary.OWL_SOME_VALUES_FROM.getIRI();
    }
}
