package org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.classexpressions.DataExactCardinality;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ObjectExactCardinality;
import org.semanticweb.sparql.owlbgp.model.properties.DataPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.properties.PropertyExpression;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;

public class TPExactCardinalityHandler extends AbstractCardinalityHandler {

    public TPExactCardinalityHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_CARDINALITY);
    }

    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        int cardinality=getCardinality(object);
        PropertyExpression pe=getPropertyExpression(subject);
        if (pe instanceof ObjectPropertyExpression) {
            ObjectPropertyExpression ope=(ObjectPropertyExpression)pe;
            consumer.mapClassIdentifierToClassExpression(subject, ObjectExactCardinality.create(cardinality,ope));
        } else if (pe instanceof DataPropertyExpression) {
            DataPropertyExpression dpe=(DataPropertyExpression)pe;
            consumer.mapClassIdentifierToClassExpression(subject, DataExactCardinality.create(cardinality,dpe));
        } else {
            // TODO: error handling
            throw new RuntimeException("error");
        }
    }
}
