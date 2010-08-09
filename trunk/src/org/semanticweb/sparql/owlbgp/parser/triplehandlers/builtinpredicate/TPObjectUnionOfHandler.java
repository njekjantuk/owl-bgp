package org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ObjectUnionOf;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.TripleHandler;

public class TPObjectUnionOfHandler extends TripleHandler {

    public TPObjectUnionOfHandler(TripleConsumer consumer) {
        super(consumer);
    }
    
    @Override
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        Set<ClassExpression> classExpressionSet=consumer.translateToClassExpressionSet(object);
        if (classExpressionSet!=null&&classExpressionSet.size()>0)
            if (classExpressionSet.size()>1)
                consumer.mapClassIdentifierToClassExpression(subject, ObjectUnionOf.create(classExpressionSet));
            else 
                consumer.mapClassIdentifierToClassExpression(subject, classExpressionSet.iterator().next());
        else {
            // TODO: error handling
            System.err.println("error");
        }
    }
}
