package org.semanticweb.sparql.owlbgpparser.translators;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.classexpressions.DataMaxCardinality;
import org.semanticweb.sparql.owlbgp.model.dataranges.DataRange;
import org.semanticweb.sparql.owlbgp.model.properties.DataPropertyExpression;
import org.semanticweb.sparql.owlbgpparser.TripleConsumer;
import org.semanticweb.sparql.owlbgpparser.Vocabulary;

public class DataMaxCardinalityTranslator extends AbstractDataCardinalityTranslator {

    public DataMaxCardinalityTranslator(TripleConsumer consumer) {
        super(consumer);
    }
    protected ClassExpression createRestriction(DataPropertyExpression prop, int cardi, DataRange filler) {
        return DataMaxCardinality.create(cardi, prop, filler);
    }
    protected Identifier getCardinalityTriplePredicate() {
        return Vocabulary.OWL_MAX_CARDINALITY.getIRI();
    }
    protected Identifier getQualifiedCardinalityTriplePredicate() {
        return Vocabulary.OWL_MAX_QUALIFIED_CARDINALITY.getIRI();
    }
}
