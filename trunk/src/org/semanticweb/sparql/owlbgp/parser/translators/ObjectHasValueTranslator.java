package org.semanticweb.sparql.owlbgp.parser.translators;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ObjectHasValue;
import org.semanticweb.sparql.owlbgp.model.individuals.Individual;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;

public class ObjectHasValueTranslator extends AbstractObjectRestrictionTranslator {

    public ObjectHasValueTranslator(TripleConsumer consumer) {
        super(consumer);
    }
    protected ClassExpression translateRestriction(Identifier mainNode) {
        Identifier hasValueObject=consumer.getResourceObject(mainNode, Vocabulary.OWL_HAS_VALUE.getIRI(), true);
        ObjectPropertyExpression prop=translateOnProperty(mainNode);
        Individual ind=consumer.translateIndividual(hasValueObject);
        return ObjectHasValue.create(prop, ind);
    }
}
