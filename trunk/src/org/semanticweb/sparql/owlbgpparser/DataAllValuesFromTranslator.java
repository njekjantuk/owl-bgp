package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.DataAllValuesFrom;
import org.semanticweb.sparql.owlbgp.model.DataPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.DataRange;

public class DataAllValuesFromTranslator extends AbstractDataQuantifiedRestrictionTranslator {

    public DataAllValuesFromTranslator(OWLRDFConsumer consumer) {
        super(consumer);
    }
    
    protected ClassExpression createRestriction(DataPropertyExpression prop,DataRange filler) {
        return DataAllValuesFrom.create(prop, filler);
    }
    protected String getFillerTriplePredicate() {
        return Vocabulary.OWL_ALL_VALUES_FROM.getIRI();
    }
}
