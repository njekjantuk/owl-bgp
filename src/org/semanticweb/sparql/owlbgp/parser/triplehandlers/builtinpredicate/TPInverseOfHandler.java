package org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Annotation;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.InverseObjectProperties;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.TripleHandler;

public class TPInverseOfHandler extends TripleHandler {

    public TPInverseOfHandler(TripleConsumer consumer) {
        super(consumer);
    }

    @Override
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object, Set<Annotation> annotations) {
        ObjectPropertyExpression subProperty=consumer.getObjectPropertyExpressionForObjectPropertyIdentifier(subject);
        ObjectPropertyExpression superProperty=consumer.getObjectPropertyExpressionForObjectPropertyIdentifier(object);
        if (subProperty!=null && superProperty!=null)
            consumer.addAxiom(InverseObjectProperties.create(subProperty,superProperty,annotations));
        else 
            throw new RuntimeException("Error: The triple "+subject+" "+predicate+" "+object+" could not be translated into an InverseObjectProperties axiom. The subproperty from the subject "+subject+" was: "+subProperty+", the superproperty from the object "+object+" was: "+superProperty+". ");
    }
}
