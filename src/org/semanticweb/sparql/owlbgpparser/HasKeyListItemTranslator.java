package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.ILiteral;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.PropertyExpression;

public class HasKeyListItemTranslator implements ListItemTranslator<PropertyExpression> {

    protected OWLRDFConsumer consumer;

    public HasKeyListItemTranslator(OWLRDFConsumer consumer) {
        this.consumer = consumer;
    }

    public PropertyExpression translate(ILiteral firstObject) {
        return null;
    }
    public PropertyExpression translate(Identifier firstObject) {
        if (consumer.isObjectPropertyOnly(firstObject)) 
            return consumer.translateObjectPropertyExpression(firstObject);
        else if (consumer.isDataPropertyOnly(firstObject))
            return consumer.translateDataPropertyExpression(firstObject);
        else throw new RuntimeException("Could not disambiguate property "+firstObject+". ");
    }
}
