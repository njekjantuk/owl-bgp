package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Facet;
import org.semanticweb.sparql.owlbgp.model.FacetRestriction;
import org.semanticweb.sparql.owlbgp.model.ILiteral;
import org.semanticweb.sparql.owlbgp.model.Literal;

public class OWLFacetRestrictionListItemTranslator implements ListItemTranslator<FacetRestriction> {

    protected final OWLRDFConsumer consumer;

    public OWLFacetRestrictionListItemTranslator(OWLRDFConsumer consumer) {
        this.consumer = consumer;
    }
    public FacetRestriction translate(ILiteral firstObject) {
        return null;
    }
    public FacetRestriction translate(String firstObject) {
        for (Facet facet : Facet.OWL_FACETS) {
            Literal lit = (Literal)consumer.getLiteralObject(firstObject, facet.getIRIString(), true);
            if (lit != null) return FacetRestriction.create(facet, lit);
        }
        return null;
    }
}
