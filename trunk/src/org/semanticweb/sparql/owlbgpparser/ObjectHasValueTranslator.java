package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.Individual;
import org.semanticweb.sparql.owlbgp.model.ObjectHasValue;
import org.semanticweb.sparql.owlbgp.model.ObjectPropertyExpression;

public class ObjectHasValueTranslator extends AbstractObjectRestrictionTranslator {

    public ObjectHasValueTranslator(OWLRDFConsumer consumer) {
        super(consumer);
    }
    protected ClassExpression translateRestriction(Identifier mainNode) {
        Identifier hasValueObject=getResourceObject(mainNode, Vocabulary.OWL_HAS_VALUE.getIRI(), true);
        ObjectPropertyExpression prop=translateOnProperty(mainNode);
        Individual ind=consumer.translateIndividual(hasValueObject);
        return ObjectHasValue.create(prop, ind);
    }
}
