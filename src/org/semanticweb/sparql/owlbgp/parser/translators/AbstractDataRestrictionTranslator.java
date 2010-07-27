package org.semanticweb.sparql.owlbgp.parser.translators;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.properties.DataPropertyExpression;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;

public abstract class AbstractDataRestrictionTranslator extends AbstractRestrictionTranslator {
    public AbstractDataRestrictionTranslator(TripleConsumer consumer) {
        super(consumer);
    }
    protected DataPropertyExpression translateOnProperty(Identifier mainNode) {
        Identifier onPropertyObject=consumer.getResourceObject(mainNode, Vocabulary.OWL_ON_PROPERTY.getIRI(), true);
        return consumer.translateDataPropertyExpression(onPropertyObject);
    }

}
