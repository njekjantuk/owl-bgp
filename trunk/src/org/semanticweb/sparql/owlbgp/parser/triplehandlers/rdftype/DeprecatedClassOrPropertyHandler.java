package org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Annotation;
import org.semanticweb.sparql.owlbgp.model.AnnotationSubject;
import org.semanticweb.sparql.owlbgp.model.AnnotationValue;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.AnnotationAssertion;
import org.semanticweb.sparql.owlbgp.model.dataranges.Datatype;
import org.semanticweb.sparql.owlbgp.model.literals.TypedLiteral;
import org.semanticweb.sparql.owlbgp.model.properties.AnnotationProperty;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.TripleHandler;

public class DeprecatedClassOrPropertyHandler extends TripleHandler {

    public DeprecatedClassOrPropertyHandler(TripleConsumer consumer) {
        super(consumer);
    }

    @Override
    public void handleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        super.handleStreaming(subject, predicate, object, false);
        if (consumer.isAnonymous(subject))
            throw new RuntimeException("Error: The subject of the triple "+subject+" "+predicate+" "+object+" is anonymous, but annotation assertions cannot have anonymous individuals as subjects. ");
    }
    @Override
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object, Set<Annotation> annotations) {
        consumer.addAxiom(AnnotationAssertion.create(AnnotationProperty.create(Vocabulary.OWL_DEPRECATED), (AnnotationSubject)subject, (AnnotationValue)TypedLiteral.create("true", null, Datatype.XSD_BOOLEAN)));
    }
}
