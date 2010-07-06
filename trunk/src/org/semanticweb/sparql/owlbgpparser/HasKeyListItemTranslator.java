package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.DataProperty;
import org.semanticweb.sparql.owlbgp.model.ILiteral;
import org.semanticweb.sparql.owlbgp.model.ObjectProperty;
import org.semanticweb.sparql.owlbgp.model.PropertyExpression;

public class HasKeyListItemTranslator implements ListItemTranslator<PropertyExpression> {

    protected OWLRDFConsumer consumer;

    public HasKeyListItemTranslator(OWLRDFConsumer consumer) {
        this.consumer = consumer;
    }

    public PropertyExpression translate(ILiteral firstObject) {
        return null;
    }
    public PropertyExpression translate(String firstObject) {
        if (consumer.isObjectPropertyOnly(firstObject)) 
            return ObjectProperty.create(firstObject);
        else if (consumer.isDataPropertyOnly(firstObject))
            return DataProperty.create(firstObject);
        else throw new RuntimeException("Could not disambiguate property "+firstObject+". ");
    }
}
