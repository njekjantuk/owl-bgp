package org.semanticweb.sparql.owlbgp.parser.translators;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.dataranges.FacetRestriction;
import org.semanticweb.sparql.owlbgp.model.dataranges.FacetRestriction.OWL2_FACET;
import org.semanticweb.sparql.owlbgp.model.literals.Literal;
import org.semanticweb.sparql.owlbgp.model.literals.TypedLiteral;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;

public class FacetRestrictionListItemTranslator implements ListItemTranslator<FacetRestriction> {

    protected final TripleConsumer consumer;

    public FacetRestrictionListItemTranslator(TripleConsumer consumer) {
        this.consumer = consumer;
    }
    public FacetRestriction translate(Identifier firstObject) {
        for (OWL2_FACET facet : OWL2_FACET.values()) {
            Literal lit=consumer.getLiteralObject(firstObject, facet.getIRI());
            if (lit!=null) {
                if (lit instanceof TypedLiteral)
                    return FacetRestriction.create(facet, (TypedLiteral)lit);
                else
                    // TODO: error handling
                    System.err.println("error");
            }
        }
        return null;
    }
}
