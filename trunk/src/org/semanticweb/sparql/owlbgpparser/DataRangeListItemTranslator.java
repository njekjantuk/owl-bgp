package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.dataranges.DataRange;
import org.semanticweb.sparql.owlbgp.model.literals.Literal;

public class DataRangeListItemTranslator implements ListItemTranslator<DataRange> {

    protected final OWLRDFConsumer consumer;

    public DataRangeListItemTranslator(OWLRDFConsumer consumer) {
        this.consumer=consumer;
    }
    public DataRange translate(Literal firstObject) {
        return null;
    }
    public DataRange translate(Identifier firstObject) {
        return consumer.translateDataRange(firstObject);
    }
}
