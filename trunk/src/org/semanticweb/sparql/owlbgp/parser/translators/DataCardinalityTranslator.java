package org.semanticweb.sparql.owlbgp.parser.translators;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.classexpressions.DataExactCardinality;
import org.semanticweb.sparql.owlbgp.model.dataranges.DataRange;
import org.semanticweb.sparql.owlbgp.model.properties.DataPropertyExpression;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;

public class DataCardinalityTranslator extends AbstractDataCardinalityTranslator {

    public DataCardinalityTranslator(TripleConsumer consumer) {
        super(consumer);
    }
    protected ClassExpression createRestriction(DataPropertyExpression prop, int cardi, DataRange filler) {
        return DataExactCardinality.create(cardi, prop, filler);
    }
    protected Identifier getCardinalityTriplePredicate() {
        return Vocabulary.OWL_CARDINALITY.getIRI();
    }
    protected Identifier getQualifiedCardinalityTriplePredicate() {
        return Vocabulary.OWL_QUALIFIED_CARDINALITY.getIRI();
    }
}
