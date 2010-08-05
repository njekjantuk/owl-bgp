package org.semanticweb.sparql.owlbgp.parser.translators;

import org.semanticweb.sparql.owlbgp.model.ExtendedOWLObject;
import org.semanticweb.sparql.owlbgp.model.Identifier;

public interface ListItemTranslator<O extends ExtendedOWLObject> {
    /**
     * The rdf:first triple that represents the item to be translated.  This triple
     * will point to something like a class expression, individual.
     * @param firstObject The rdf:first triple that points to the item to be translated.
     * @return The translated item.
     */
    O translate(Identifier firstObject);
}