package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.DataMinCardinality;
import org.semanticweb.sparql.owlbgp.model.DataPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.DataRange;

public class DataMinCardinalityTranslator extends AbstractDataCardinalityTranslator {

    public DataMinCardinalityTranslator(OWLRDFConsumer consumer) {
        super(consumer);
    }
    protected ClassExpression createRestriction(DataPropertyExpression prop, int cardi, DataRange filler) {
        return DataMinCardinality.create(cardi, prop, filler);
    }
    protected String getCardinalityTriplePredicate() {
        return Vocabulary.OWL_MIN_CARDINALITY.getIRI();
    }
    protected String getQualifiedCardinalityTriplePredicate() {
        return Vocabulary.OWL_MIN_QUALIFIED_CARDINALITY.getIRI();
    }
}
