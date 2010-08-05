package org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Annotation;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.Declaration;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassVariable;
import org.semanticweb.sparql.owlbgp.model.classexpressions.Clazz;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;

public class ClassHandler extends BuiltInTypeHandler {

    public ClassHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_CLASS);
    }

    @Override
    public void handleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        super.handleStreaming(subject, predicate, object, false);
        if (consumer.isVariable(subject))
            consumer.mapClassIdentifierToClassExpression(subject, ClassVariable.create(subject.toString()));
        else if (!consumer.isAnonymous(subject))
            consumer.mapClassIdentifierToClassExpression(subject, Clazz.create(subject.toString()));
    }
    @Override
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object, Set<Annotation> annotations) {
        if (consumer.isVariable(subject))
            consumer.addAxiom(Declaration.create((ClassVariable)consumer.getClassExpressionForClassIdentifier(subject),annotations));
        else 
            consumer.addAxiom(Declaration.create((Clazz)consumer.getClassExpressionForClassIdentifier(subject),annotations));
    }
}
