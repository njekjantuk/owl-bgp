package org.semanticweb.sparql.owlbgpparser.translators;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.dataranges.DataRange;
import org.semanticweb.sparql.owlbgp.model.literals.Literal;
import org.semanticweb.sparql.owlbgpparser.TripleConsumer;

public class DataRangeListItemTranslator implements ListItemTranslator<DataRange> {

    protected final TripleConsumer consumer;

    public DataRangeListItemTranslator(TripleConsumer consumer) {
        this.consumer=consumer;
    }
    public DataRange translate(Literal firstObject) {
        return null;
    }
    public DataRange translate(Identifier firstObject) {
        return consumer.translateDataRange(firstObject);
    }
}
