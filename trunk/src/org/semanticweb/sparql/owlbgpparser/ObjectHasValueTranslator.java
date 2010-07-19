package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ObjectHasValue;
import org.semanticweb.sparql.owlbgp.model.individuals.Individual;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;

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
