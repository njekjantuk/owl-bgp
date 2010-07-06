package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.DataExactCardinality;
import org.semanticweb.sparql.owlbgp.model.DataPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.DataRange;

public class DataCardinalityTranslator extends AbstractDataCardinalityTranslator {

    public DataCardinalityTranslator(OWLRDFConsumer consumer) {
        super(consumer);
    }
    protected ClassExpression createRestriction(DataPropertyExpression prop, int cardi, DataRange filler) {
        return DataExactCardinality.create(cardi, prop, filler);
    }
    protected String getCardinalityTriplePredicate() {
        return OWLRDFVocabulary.OWL_CARDINALITY.getIRI();
    }
    protected String getQualifiedCardinalityTriplePredicate() {
        return OWLRDFVocabulary.OWL_QUALIFIED_CARDINALITY.getIRI();
    }
}
