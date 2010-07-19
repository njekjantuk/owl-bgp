package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.dataranges.Facet;
import org.semanticweb.sparql.owlbgp.model.dataranges.FacetRestriction;
import org.semanticweb.sparql.owlbgp.model.literals.Literal;
import org.semanticweb.sparql.owlbgp.model.literals.TypedLiteral;

public class FacetRestrictionListItemTranslator implements ListItemTranslator<FacetRestriction> {

    protected final OWLRDFConsumer consumer;

    public FacetRestrictionListItemTranslator(OWLRDFConsumer consumer) {
        this.consumer = consumer;
    }
    public FacetRestriction translate(Literal firstObject) {
        return null;
    }
    public FacetRestriction translate(Identifier firstObject) {
        for (Facet facet : Facet.OWL_FACETS) {
            TypedLiteral lit = (TypedLiteral)consumer.getLiteralObject(firstObject, facet.getIdentifier(), true);
            if (lit != null) return FacetRestriction.create(facet, lit);
        }
        return null;
    }
}
