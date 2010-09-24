package org.semanticweb.sparql.owlbgp.parser.translators;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.dataranges.DataRange;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;

public class DataRangeListItemTranslator implements ListItemTranslator<DataRange> {

    protected final TripleConsumer consumer;

    public DataRangeListItemTranslator(TripleConsumer consumer) {
        this.consumer=consumer;
    }
    public DataRange translate(Identifier firstObject) {
        consumer.translateDataRange(firstObject);
        return consumer.getDR(firstObject);
    }
}
