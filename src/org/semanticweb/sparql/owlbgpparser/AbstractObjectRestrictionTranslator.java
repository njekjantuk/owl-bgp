package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.ObjectPropertyExpression;

public abstract class AbstractObjectRestrictionTranslator extends AbstractRestrictionTranslator {

    public AbstractObjectRestrictionTranslator(OWLRDFConsumer consumer) {
        super(consumer);
    }
    //Translates and consumes the onProperty triple, creating an object property (expression) corresponding to the object
    // of the onProperty triple.
    protected ObjectPropertyExpression translateOnProperty(Identifier mainNode) {
        Identifier onPropertyIRI=getConsumer().getResourceObject(mainNode, Vocabulary.OWL_ON_PROPERTY.getIRI(), true);
        if (onPropertyIRI == null) return null;
        return consumer.translateObjectPropertyExpression(onPropertyIRI);
    }
}
