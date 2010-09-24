package org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Annotation;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.Declaration;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectProperty;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyVariable;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.TripleHandler;

public class ObjectPropertyHandler extends TripleHandler {

    public ObjectPropertyHandler(TripleConsumer consumer) {
        super(consumer);
    }

    @Override
    public void handleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        super.handleStreaming(subject, predicate, object, false);
        if (consumer.isVariable(subject))
            consumer.mapObjectPropertyIdentifierToObjectProperty(subject, ObjectPropertyVariable.create(subject.toString()));
        else if (consumer.isAnonymous(subject))
            throw new IllegalArgumentException("The subject of a declaration triple cannot be a blank node, but here we have the triple: "+subject+" "+predicate+" "+object);
        else
            consumer.mapObjectPropertyIdentifierToObjectProperty(subject, ObjectProperty.create(subject.toString()));
    }
    @Override
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object, Set<Annotation> annotations) {
        if (consumer.isVariable(subject))
            consumer.addAxiom(Declaration.create((ObjectPropertyVariable)consumer.getOPE(subject),annotations));
        else 
            consumer.addAxiom(Declaration.create((ObjectProperty)consumer.getOPE(subject),annotations));
    }
}
