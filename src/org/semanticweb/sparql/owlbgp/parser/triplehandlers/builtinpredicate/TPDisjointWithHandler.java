package org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Annotation;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.DisjointClasses;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.TriplePredicateHandler;

public class TPDisjointWithHandler extends TriplePredicateHandler {

    public TPDisjointWithHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_DISJOINT_WITH);
    }

    @Override
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object, Set<Annotation> annotations) {
        ClassExpression class1=consumer.getClassExpressionForClassIdentifier(subject);
        ClassExpression class2=consumer.getClassExpressionForClassIdentifier(object);
        String errorMessage="";
        if (class1==null)
            errorMessage="Could not find a class expression for the subject in the triple "+subject+" owl:equivalentClass "+object+". ";
        if (class2==null)
            errorMessage+="Could not find a class expression for the object in the triple "+subject+" owl:equivalentClass "+object+". ";
        if (class1!=null && class2!=null) {
            Set<ClassExpression> classes=new HashSet<ClassExpression>();
            classes.add(class1);
            classes.add(class2);
            consumer.addAxiom(DisjointClasses.create(classes,annotations));
        } else {
            throw new RuntimeException(errorMessage);
        }
    }
}
