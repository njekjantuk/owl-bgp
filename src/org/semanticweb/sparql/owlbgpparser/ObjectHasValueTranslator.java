package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.AnonymousIndividual;
import org.semanticweb.sparql.owlbgp.model.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.Individual;
import org.semanticweb.sparql.owlbgp.model.IndividualVariable;
import org.semanticweb.sparql.owlbgp.model.NamedIndividual;
import org.semanticweb.sparql.owlbgp.model.ObjectHasValue;
import org.semanticweb.sparql.owlbgp.model.ObjectPropertyExpression;

public class ObjectHasValueTranslator extends AbstractObjectRestrictionTranslator {

    public ObjectHasValueTranslator(OWLRDFConsumer consumer) {
        super(consumer);
    }
    protected ClassExpression translateRestriction(String mainNode) {
        String hasValueObject=getResourceObject(mainNode, OWLRDFVocabulary.OWL_HAS_VALUE.getIRI(), true);
        ObjectPropertyExpression prop=translateOnProperty(mainNode);
        Individual ind;
        if (consumer.isAnonymousNode(hasValueObject)) ind=AnonymousIndividual.create(hasValueObject);
        else if (consumer.isVariableNode(hasValueObject)) ind=IndividualVariable.create(hasValueObject);
        else ind=NamedIndividual.create(hasValueObject);
        consumer.addIndividual(ind.getIdentifier());
        return ObjectHasValue.create(prop, ind);
    }
}
