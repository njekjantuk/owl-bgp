package org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Annotation;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.TransitiveObjectProperty;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectProperty;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyVariable;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.TripleHandler;

public class TransitivePropertyHandler extends TripleHandler {

    public TransitivePropertyHandler(TripleConsumer consumer) {
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
        ObjectPropertyExpression ope=consumer.getOPE(subject);
        if (ope!=null) {
            consumer.addAxiom(TransitiveObjectProperty.create(ope,annotations));
        } else
            throw new RuntimeException("Error: The subject of the triple "+subject+" "+predicate+" "+object+" could not be translated into an object property. ");
    }
}
