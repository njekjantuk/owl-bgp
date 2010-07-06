package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.DataMaxCardinality;
import org.semanticweb.sparql.owlbgp.model.DataPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.DataRange;

public class DataMaxCardinalityTranslator extends AbstractDataCardinalityTranslator {

    public DataMaxCardinalityTranslator(OWLRDFConsumer consumer) {
        super(consumer);
    }
    protected ClassExpression createRestriction(DataPropertyExpression prop, int cardi, DataRange filler) {
        return DataMaxCardinality.create(cardi, prop, filler);
    }
    protected String getCardinalityTriplePredicate() {
        return OWLRDFVocabulary.OWL_MAX_CARDINALITY.getIRI();
    }
    protected String getQualifiedCardinalityTriplePredicate() {
        return OWLRDFVocabulary.OWL_MAX_QUALIFIED_CARDINALITY.getIRI();
    }
}
