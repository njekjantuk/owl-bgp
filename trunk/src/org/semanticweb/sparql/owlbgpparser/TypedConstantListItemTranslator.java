package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.ILiteral;
import org.semanticweb.sparql.owlbgp.model.Identifier;

public class TypedConstantListItemTranslator implements ListItemTranslator<ILiteral> {
    protected final OWLRDFConsumer consumer; 
    
    public TypedConstantListItemTranslator(OWLRDFConsumer consumer) {
    	this.consumer=consumer;
    }
    public ILiteral translate(Identifier firstObject) {
        throw new IllegalArgumentException("Cannot translate list item to a constant because rdf:first triple is a resource triple");
    }
    public ILiteral translate(ILiteral firstObject) {
    	return firstObject;
    }
}
