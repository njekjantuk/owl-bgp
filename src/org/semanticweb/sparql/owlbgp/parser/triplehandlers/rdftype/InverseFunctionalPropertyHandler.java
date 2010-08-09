package org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Annotation;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.InverseFunctionalObjectProperty;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectProperty;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyVariable;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.AbstractResourceTripleHandler;

public class InverseFunctionalPropertyHandler extends AbstractResourceTripleHandler {

    public InverseFunctionalPropertyHandler(TripleConsumer consumer) {
        super(consumer);
    }

    @Override
    public void handleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        super.handleStreaming(subject, predicate, object, false);
        if (consumer.isVariable(subject))
            consumer.mapObjectPropertyIdentifierToObjectProperty(subject, ObjectPropertyVariable.create(subject.toString()));
        else if (!consumer.isAnonymous(subject))
            consumer.mapObjectPropertyIdentifierToObjectProperty(subject, ObjectProperty.create(subject.toString()));
    }
    @Override
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object, Set<Annotation> annotations) {
        ObjectPropertyExpression ope=consumer.getObjectPropertyExpressionForObjectPropertyIdentifier(subject);
        if (ope!=null)
            consumer.addAxiom(InverseFunctionalObjectProperty.create(ope,annotations));
        else
            throw new RuntimeException("Error: The subject of the triple "+subject+" "+predicate+" "+object+" could not be translated into an object property. ");
    }
}
