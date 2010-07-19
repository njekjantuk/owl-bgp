package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.properties.DataPropertyExpression;

public abstract class AbstractDataRestrictionTranslator extends AbstractRestrictionTranslator {
    public AbstractDataRestrictionTranslator(OWLRDFConsumer consumer) {
        super(consumer);
    }
    protected DataPropertyExpression translateOnProperty(Identifier mainNode) {
        Identifier onPropertyObject=getResourceObject(mainNode, Vocabulary.OWL_ON_PROPERTY.getIRI(), true);
        return consumer.translateDataPropertyExpression(onPropertyObject);
    }

}
