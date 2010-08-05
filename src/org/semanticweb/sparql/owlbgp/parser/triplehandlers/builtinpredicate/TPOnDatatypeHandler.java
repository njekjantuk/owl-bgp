package org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.dataranges.DataRange;
import org.semanticweb.sparql.owlbgp.model.dataranges.Datatype;
import org.semanticweb.sparql.owlbgp.model.dataranges.DatatypeRestriction;
import org.semanticweb.sparql.owlbgp.model.dataranges.FacetRestriction;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.TriplePredicateHandler;

public class TPOnDatatypeHandler extends TriplePredicateHandler {

    public TPOnDatatypeHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_INTERSECTION_OF);
    }

    @Override
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        consumer.translateDataRange(object);
        DataRange dataRange=consumer.getDataRangeForDataRangeIdentifier(object);
        if (dataRange!=null && dataRange instanceof Datatype) {
            Datatype datatype=(Datatype)dataRange;
            // get facet restrictions
            Set<FacetRestriction> facetRestrictions=consumer.translateToFacetRestrictionSet(subject);
            if (facetRestrictions!=null && facetRestrictions.size()>0)
                consumer.mapDataRangeIdentifierToDataRange(subject, DatatypeRestriction.create(datatype,facetRestrictions));
            else
                // TODO: error handling
                consumer.mapDataRangeIdentifierToDataRange(subject, datatype);
        } else
            // TODO: error handling
            System.err.println("error");
    }
}
