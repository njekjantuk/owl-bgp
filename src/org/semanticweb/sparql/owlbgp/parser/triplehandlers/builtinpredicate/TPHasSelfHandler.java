package org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ObjectHasSelf;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.AbstractResourceTripleHandler;

public class TPHasSelfHandler extends AbstractResourceTripleHandler {

    public TPHasSelfHandler(TripleConsumer consumer) {
        super(consumer);
    }

    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        Identifier propID=consumer.getObject(subject, Vocabulary.OWL_ON_PROPERTY, true);
        ObjectPropertyExpression ope=consumer.getObjectPropertyExpressionForObjectPropertyIdentifier(propID);
        if (ope!=null) {
            consumer.mapClassIdentifierToClassExpression(subject, ObjectHasSelf.create(ope));
        } else {
            // TODO: error handling
            throw new RuntimeException("error");
        }
    }
}
