package org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.classexpressions.DataExactCardinality;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ObjectExactCardinality;
import org.semanticweb.sparql.owlbgp.model.dataranges.DataRange;
import org.semanticweb.sparql.owlbgp.model.properties.DataPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.properties.PropertyExpression;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;

public class TPExactQualifiedCardinalityHandler extends AbstractCardinalityHandler {

    public TPExactQualifiedCardinalityHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_CARDINALITY);
    }

    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        int cardinality=getCardinality(object);
        PropertyExpression pe=getPropertyExpression(subject);
        if (pe instanceof ObjectPropertyExpression) {
            ObjectPropertyExpression ope=(ObjectPropertyExpression)pe;
            ClassExpression ce=getClassExpression(subject);
            consumer.mapClassIdentifierToClassExpression(subject, ObjectExactCardinality.create(cardinality,ope,ce));
        } else if (pe instanceof DataPropertyExpression) {
            DataPropertyExpression dpe=(DataPropertyExpression)pe;
            DataRange dr=getDataRange(subject);
            consumer.mapClassIdentifierToClassExpression(subject, DataExactCardinality.create(cardinality,dpe,dr));
        } else {
            // TODO: error handling
            throw new RuntimeException("error");
        }
    }
}
