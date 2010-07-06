package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.DataRange;
import org.semanticweb.sparql.owlbgp.model.ILiteral;

public class DataRangeListItemTranslator implements ListItemTranslator<DataRange> {

    protected final OWLRDFConsumer consumer;

    public DataRangeListItemTranslator(OWLRDFConsumer consumer) {
        this.consumer=consumer;
    }
    public DataRange translate(ILiteral firstObject) {
        return null;
    }
    public DataRange translate(String firstObject) {
        return consumer.translateDataRange(firstObject);
    }
}
