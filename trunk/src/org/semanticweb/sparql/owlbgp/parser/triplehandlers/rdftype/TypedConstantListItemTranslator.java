package org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.literals.Literal;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.translators.ListItemTranslator;

public class TypedConstantListItemTranslator implements ListItemTranslator<Literal> {
    protected final TripleConsumer consumer; 
    
    public TypedConstantListItemTranslator(TripleConsumer consumer) {
    	this.consumer=consumer;
    }
    public Literal translate(Identifier firstObject) {
        throw new IllegalArgumentException("Cannot translate list item to a constant because rdf:first triple is a resource triple");
    }
    public Literal translate(Literal firstObject) {
    	return firstObject;
    }
}
