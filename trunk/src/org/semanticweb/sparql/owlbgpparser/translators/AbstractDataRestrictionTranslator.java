package org.semanticweb.sparql.owlbgpparser.translators;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.properties.DataPropertyExpression;
import org.semanticweb.sparql.owlbgpparser.TripleConsumer;
import org.semanticweb.sparql.owlbgpparser.Vocabulary;

public abstract class AbstractDataRestrictionTranslator extends AbstractRestrictionTranslator {
    public AbstractDataRestrictionTranslator(TripleConsumer consumer) {
        super(consumer);
    }
    protected DataPropertyExpression translateOnProperty(Identifier mainNode) {
        Identifier onPropertyObject=consumer.getResourceObject(mainNode, Vocabulary.OWL_ON_PROPERTY.getIRI(), true);
        return consumer.translateDataPropertyExpression(onPropertyObject);
    }

}
