package org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.dataranges.DataComplementOf;
import org.semanticweb.sparql.owlbgp.model.dataranges.DataRange;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.TripleHandler;

public class TPDataComplementOfHandler extends TripleHandler {

    public TPDataComplementOfHandler(TripleConsumer consumer) {
        super(consumer);
    }

    @Override
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        consumer.translateDataRange(object);
        DataRange dataRange=consumer.getDR(object);
        if (dataRange!=null)
            consumer.mapDataRangeIdentifierToDataRange(subject, DataComplementOf.create(dataRange));
        else {
            // TODO: error handling
            throw new RuntimeException("error");
        }
    }
}
