package org.semanticweb.sparql.owlbgp.parser.translators;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;

public abstract class AbstractObjectRestrictionTranslator extends AbstractRestrictionTranslator {

    public AbstractObjectRestrictionTranslator(TripleConsumer consumer) {
        super(consumer);
    }
    //Translates and consumes the onProperty triple, creating an object property (expression) corresponding to the object
    // of the onProperty triple.
    protected ObjectPropertyExpression translateOnProperty(Identifier mainNode) {
        Identifier onPropertyIRI=consumer.getResourceObject(mainNode, Vocabulary.OWL_ON_PROPERTY.getIRI(), true);
        if (onPropertyIRI == null) return null;
        return consumer.translateObjectPropertyExpression(onPropertyIRI);
    }
}
