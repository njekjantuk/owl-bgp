package org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Annotation;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.DisjointClasses;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;

public class AllDisjointClassesHandler extends BuiltInTypeHandler {

    public AllDisjointClassesHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_ALL_DISJOINT_CLASSES);
    }
    
    @Override
    public void handleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        super.handleStreaming(subject, predicate, object, false);
        consumer.addReifiedSubject(subject);
    }
    
    @Override
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object, Set<Annotation> annotations) {
        Identifier listMainNode=consumer.getObject(subject, Vocabulary.OWL_MEMBERS, true);
        if (listMainNode==null)
            throw new RuntimeException("Error: There is not list with main node "+subject+", which contains the classes for the triple "+subject+" "+predicate+" "+object);
        else {
            Set<ClassExpression> classExpressionSet=consumer.translateToClassExpressionSet(listMainNode);
            if (classExpressionSet!=null&&classExpressionSet.size()>0)
                if (classExpressionSet.size()>1)
                    consumer.addAxiom(DisjointClasses.create(classExpressionSet,annotations));
                else 
                    throw new RuntimeException("Error: A list representing the classes of a DisjointClasses axiom has less than 2 items in it. The main triple is: "+subject+" "+Vocabulary.OWL_MEMBERS.toString()+" "+listMainNode+" and "+listMainNode+" is the main node for the list. ");
            else 
                throw new RuntimeException("Error: A list representing the classes of a DisjointClasses axiom could not be assembled. The main triple is: "+subject+" "+Vocabulary.OWL_MEMBERS.toString()+" "+listMainNode+" and "+listMainNode+" is the main node for the list. ");
        }
    }
}
