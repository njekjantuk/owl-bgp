package org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.classexpressions.DataMinCardinality;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ObjectMinCardinality;
import org.semanticweb.sparql.owlbgp.model.properties.DataPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.properties.PropertyExpression;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;

public class TPMinCardinalityHandler extends AbstractCardinalityHandler {

    public TPMinCardinalityHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_MIN_CARDINALITY);
    }

    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        int cardinality=getCardinality(object);
        PropertyExpression pe=getPropertyExpression(subject);
        if (pe instanceof ObjectPropertyExpression) {
            ObjectPropertyExpression ope=(ObjectPropertyExpression)pe;
            consumer.mapClassIdentifierToClassExpression(subject, ObjectMinCardinality.create(cardinality,ope));
        } else if (pe instanceof DataPropertyExpression) {
            DataPropertyExpression dpe=(DataPropertyExpression)pe;
            consumer.mapClassIdentifierToClassExpression(subject, DataMinCardinality.create(cardinality,dpe));
        } else {
            // TODO: error handling
            throw new RuntimeException("error");
        }
    }
}
