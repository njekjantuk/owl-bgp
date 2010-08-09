package org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Annotation;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.Declaration;
import org.semanticweb.sparql.owlbgp.model.properties.AnnotationProperty;
import org.semanticweb.sparql.owlbgp.model.properties.AnnotationPropertyVariable;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.TripleHandler;

public class AnnotationPropertyHandler extends TripleHandler {

    public AnnotationPropertyHandler(TripleConsumer consumer) {
        super(consumer);
    }

    @Override
    public void handleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        super.handleStreaming(subject, predicate, object, false);
        if (consumer.isVariable(subject))
            consumer.mapAnnotationPropertyIdentifierToAnnotationProperty(subject, AnnotationPropertyVariable.create(subject.toString()));
        else if (consumer.isAnonymous(subject))
            throw new IllegalArgumentException("The subject of an annotation property declaration triple cannot be a blank node, but here we have the triple: "+subject+" "+predicate+" "+object);
        else
            consumer.mapAnnotationPropertyIdentifierToAnnotationProperty(subject, AnnotationProperty.create(subject.toString()));
    }
    @Override
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object, Set<Annotation> annotations) {
        if (consumer.isVariable(subject))
            consumer.addAxiom(Declaration.create((AnnotationPropertyVariable)consumer.getAnnotationPropertyExpressionForAnnotationPropertyIdentifier(subject),annotations));
        else 
            consumer.addAxiom(Declaration.create((AnnotationProperty)consumer.getAnnotationPropertyExpressionForAnnotationPropertyIdentifier(subject),annotations));
    }
}
