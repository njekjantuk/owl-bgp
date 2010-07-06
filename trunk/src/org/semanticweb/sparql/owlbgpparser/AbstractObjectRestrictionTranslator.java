package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.ObjectPropertyExpression;

public abstract class AbstractObjectRestrictionTranslator extends AbstractRestrictionTranslator {

    public AbstractObjectRestrictionTranslator(OWLRDFConsumer consumer) {
        super(consumer);
    }
    //Translates and consumes the onProperty triple, creating an object property (expression) corresponding to the object
    // of the onProperty triple.
    protected ObjectPropertyExpression translateOnProperty(String mainNode) {
        String onPropertyIRI=getConsumer().getResourceObject(mainNode, OWLRDFVocabulary.OWL_ON_PROPERTY.getIRI(), true);
        if (onPropertyIRI == null) return null;
        return getConsumer().translateObjectPropertyExpression(onPropertyIRI);
    }
}
