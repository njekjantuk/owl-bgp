package org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.classexpressions.DataAllValuesFrom;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ObjectAllValuesFrom;
import org.semanticweb.sparql.owlbgp.model.dataranges.DataRange;
import org.semanticweb.sparql.owlbgp.model.properties.DataPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.TripleHandler;

public class TPAllValuesFromHandler extends TripleHandler {

    public TPAllValuesFromHandler(TripleConsumer consumer) {
        super(consumer);
    }

    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        Identifier propID=consumer.getObject(subject, Vocabulary.OWL_ON_PROPERTY,true);
        ObjectPropertyExpression ope=consumer.getObjectPropertyExpressionForObjectPropertyIdentifier(propID);
        if (ope!=null) {
            consumer.translateClassExpression(object);
            ClassExpression ce=consumer.getClassExpressionForClassIdentifier(object);
            consumer.mapClassIdentifierToClassExpression(subject, ObjectAllValuesFrom.create(ope, ce));
        } else {
            DataPropertyExpression dpe=consumer.getDataPropertyExpressionForDataPropertyIdentifier(propID);
            if (dpe!=null) {
                DataRange dr=consumer.getDataRangeForDataRangeIdentifier(object);
                if (dr!=null) {
                    consumer.mapClassIdentifierToClassExpression(subject, DataAllValuesFrom.create(dpe, dr));
                } else {
                    // TODO: error handling
                    throw new RuntimeException("error");
                }
            } else {
                // TODO: error handling
                throw new RuntimeException("error");
            }
        }
    }
}
