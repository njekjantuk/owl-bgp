package org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.classexpressions.DataHasValue;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ObjectHasValue;
import org.semanticweb.sparql.owlbgp.model.individuals.Individual;
import org.semanticweb.sparql.owlbgp.model.literals.Literal;
import org.semanticweb.sparql.owlbgp.model.properties.DataPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.TripleHandler;

public class TPHasValueHandler extends TripleHandler {

    public TPHasValueHandler(TripleConsumer consumer) {
        super(consumer);
    }

    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        Identifier propID=consumer.getObject(subject, Vocabulary.OWL_ON_PROPERTY,true);
        if (object instanceof Literal) {
            DataPropertyExpression dpe=consumer.getDPE(propID);
            if (dpe!=null) {
                consumer.mapClassIdentifierToClassExpression(subject, DataHasValue.create(dpe, (Literal)object));
            } else {
                // TODO: error handling
                throw new RuntimeException("error");
            }
        } else {
            ObjectPropertyExpression ope=consumer.getOPE(propID);
            if (ope!=null) {
                if (consumer.isAnonymous(object))
                    throw new IllegalArgumentException("Error: The individuals in a hasValue expression cannot be anonymous, but here we have the triple: "+subject+" "+predicate+" "+object);
                Individual ind=consumer.getIND(object);
                consumer.mapClassIdentifierToClassExpression(subject, ObjectHasValue.create(ope,ind));
            } else {
                // TODO: error handling
                throw new RuntimeException("error");
            }
        }
    }
}
