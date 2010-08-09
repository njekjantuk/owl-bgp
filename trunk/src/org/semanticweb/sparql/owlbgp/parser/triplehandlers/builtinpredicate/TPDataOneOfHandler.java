package org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.dataranges.DataOneOf;
import org.semanticweb.sparql.owlbgp.model.literals.Literal;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.TripleHandler;

public class TPDataOneOfHandler extends TripleHandler {

    public TPDataOneOfHandler(TripleConsumer consumer) {
        super(consumer);
    }

    @Override
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        Set<Literal> literalSet=consumer.translateToLiteralSet(object);
        if (literalSet!=null&&literalSet.size()>0)
            consumer.mapDataRangeIdentifierToDataRange(subject, DataOneOf.create(literalSet));
        else {
            // TODO: error handling
            throw new RuntimeException("error");
        }
    }
}
