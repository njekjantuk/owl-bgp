package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.ExtendedOWLObject;
import org.semanticweb.sparql.owlbgp.model.ILiteral;

public interface ListItemTranslator<O extends ExtendedOWLObject> {
    /**
     * The rdf:first triple that represents the item to be translated.  This triple
     * will point to something like a class expression, individual.
     * @param firstObject The rdf:first triple that points to the item to be translated.
     * @return The translated item.
     */
    O translate(String firstObject);
    O translate(ILiteral firstObject);
}