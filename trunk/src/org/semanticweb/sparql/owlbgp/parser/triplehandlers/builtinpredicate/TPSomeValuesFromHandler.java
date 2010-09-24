package org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.classexpressions.DataSomeValuesFrom;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ObjectSomeValuesFrom;
import org.semanticweb.sparql.owlbgp.model.dataranges.DataRange;
import org.semanticweb.sparql.owlbgp.model.properties.DataPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.TripleHandler;

public class TPSomeValuesFromHandler extends TripleHandler {

    public TPSomeValuesFromHandler(TripleConsumer consumer) {
        super(consumer);
    }
    
    @Override
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        Identifier propID=consumer.getObject(subject, Vocabulary.OWL_ON_PROPERTY,true);
        ObjectPropertyExpression ope=consumer.getOPE(propID);
        if (ope!=null) {
            consumer.translateClassExpression(object);
            ClassExpression ce=consumer.getCE(object);
            consumer.mapClassIdentifierToClassExpression(subject, ObjectSomeValuesFrom.create(ope, ce));
        } else {
            DataPropertyExpression dpe=consumer.getDPE(propID);
            if (dpe!=null) {
                DataRange dr=consumer.getDR(object);
                if (dr!=null) {
                    consumer.mapClassIdentifierToClassExpression(subject, DataSomeValuesFrom.create(dpe, dr));
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
