package org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.dataranges.DataComplementOf;
import org.semanticweb.sparql.owlbgp.model.dataranges.DataRange;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.TriplePredicateHandler;

public class TPDataComplementOfHandler extends TriplePredicateHandler {

    public TPDataComplementOfHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_DATATYPE_COMPLEMENT_OF);
    }

    @Override
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        consumer.translateDataRange(object);
        DataRange dataRange=consumer.getDataRangeForDataRangeIdentifier(object);
        if (dataRange!=null)
            consumer.mapDataRangeIdentifierToDataRange(subject, DataComplementOf.create(dataRange));
        else {
            // TODO: error handling
        }
    }
}
