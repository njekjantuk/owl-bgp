package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Facet;
import org.semanticweb.sparql.owlbgp.model.FacetRestriction;
import org.semanticweb.sparql.owlbgp.model.ILiteral;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.Literal;

public class FacetRestrictionListItemTranslator implements ListItemTranslator<FacetRestriction> {

    protected final OWLRDFConsumer consumer;

    public FacetRestrictionListItemTranslator(OWLRDFConsumer consumer) {
        this.consumer = consumer;
    }
    public FacetRestriction translate(ILiteral firstObject) {
        return null;
    }
    public FacetRestriction translate(Identifier firstObject) {
        for (Facet facet : Facet.OWL_FACETS) {
            Literal lit = (Literal)consumer.getLiteralObject(firstObject, facet.getIdentifier(), true);
            if (lit != null) return FacetRestriction.create(facet, lit);
        }
        return null;
    }
}
