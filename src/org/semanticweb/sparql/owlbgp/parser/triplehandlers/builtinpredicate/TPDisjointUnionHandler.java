package org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Annotation;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.DisjointUnion;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassVariable;
import org.semanticweb.sparql.owlbgp.model.classexpressions.Clazz;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.TripleHandler;

public class TPDisjointUnionHandler extends TripleHandler {

    public TPDisjointUnionHandler(TripleConsumer consumer) {
        super(consumer);
    }
    
    @Override
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object,Set<Annotation> annotations) {
        ClassExpression cls=consumer.getClassExpressionForClassIdentifier(subject);
        Set<ClassExpression> classExpressionSet;
        if (cls==null) 
            throw new RuntimeException("Error: The class for a disjoint union axiom could not be found. The class is represented by the node "+subject+" in the triple: "+subject+" "+predicate+" "+object);
        else
            classExpressionSet=consumer.translateToClassExpressionSet(object);
        if (classExpressionSet==null||classExpressionSet.size()<2)
            throw new RuntimeException("Error: A list representing the classes of a disjoint union of axiom has less than 2 items in it. The main triple is: "+subject+" "+Vocabulary.OWL_DISJOINT_UNION_OF.toString()+" "+object+" and "+object+" is the main node for the list. ");
        if (cls instanceof Clazz)
            consumer.addAxiom(DisjointUnion.create((Clazz)cls,classExpressionSet,annotations));
        else if (cls instanceof ClassVariable)
            consumer.addAxiom(DisjointUnion.create((ClassVariable)cls,classExpressionSet,annotations));
        else 
            throw new RuntimeException("Error: The class for a disjoint union axiom is neither a class nor a class variable, but a class expression: "+cls+". The class is represented by the node "+subject+" in the triple: "+subject+" "+predicate+" "+object);
    }
}
