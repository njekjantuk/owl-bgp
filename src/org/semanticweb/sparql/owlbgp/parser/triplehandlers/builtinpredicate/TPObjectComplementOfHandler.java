package org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ObjectComplementOf;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.TriplePredicateHandler;

public class TPObjectComplementOfHandler extends TriplePredicateHandler {

    public TPObjectComplementOfHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_COMPLEMENT_OF);
    }

    @Override
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        consumer.translateClassExpression(object);
        ClassExpression classExpression=consumer.getClassExpressionForClassIdentifier(object);
        if (classExpression!=null)
            consumer.mapClassIdentifierToClassExpression(subject, ObjectComplementOf.create(classExpression));
        else {
            // TODO: error handling
        }
    }
}
