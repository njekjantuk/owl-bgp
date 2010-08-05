package org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.classexpressions.DataMaxCardinality;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ObjectMaxCardinality;
import org.semanticweb.sparql.owlbgp.model.properties.DataPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.properties.PropertyExpression;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;

public class TPMaxCardinalityHandler extends AbstractCardinalityHandler {

    public TPMaxCardinalityHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_MAX_CARDINALITY);
    }

    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        int cardinality=getCardinality(object);
        PropertyExpression pe=getPropertyExpression(subject);
        if (pe instanceof ObjectPropertyExpression) {
            ObjectPropertyExpression ope=(ObjectPropertyExpression)pe;
            consumer.mapClassIdentifierToClassExpression(subject, ObjectMaxCardinality.create(cardinality,ope));
        } else if (pe instanceof DataPropertyExpression) {
            DataPropertyExpression dpe=(DataPropertyExpression)pe;
            consumer.mapClassIdentifierToClassExpression(subject, DataMaxCardinality.create(cardinality,dpe));
        } else {
            // TODO: error handling
            throw new RuntimeException("error");
        }
    }
}
