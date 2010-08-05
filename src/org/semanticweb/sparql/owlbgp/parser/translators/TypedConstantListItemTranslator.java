package org.semanticweb.sparql.owlbgp.parser.translators;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.literals.Literal;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;

public class TypedConstantListItemTranslator implements ListItemTranslator<Literal> {
    protected final TripleConsumer consumer; 
    
    public TypedConstantListItemTranslator(TripleConsumer consumer) {
    	this.consumer=consumer;
    }
    public Literal translate(Identifier firstObject) {
        if (firstObject instanceof Literal) 
            return (Literal)firstObject;
        else 
            throw new IllegalArgumentException("Cannot translate list item to a constant because rdf:first triple is a resource triple");
    }
}
