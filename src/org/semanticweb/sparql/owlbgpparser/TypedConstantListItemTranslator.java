package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.literals.Literal;

public class TypedConstantListItemTranslator implements ListItemTranslator<Literal> {
    protected final OWLRDFConsumer consumer; 
    
    public TypedConstantListItemTranslator(OWLRDFConsumer consumer) {
    	this.consumer=consumer;
    }
    public Literal translate(Identifier firstObject) {
        throw new IllegalArgumentException("Cannot translate list item to a constant because rdf:first triple is a resource triple");
    }
    public Literal translate(Literal firstObject) {
    	return firstObject;
    }
}
