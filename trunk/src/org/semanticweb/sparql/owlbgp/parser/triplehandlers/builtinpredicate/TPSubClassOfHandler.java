package org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Annotation;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.SubClassOf;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.TripleHandler;

public class TPSubClassOfHandler extends TripleHandler {

    public TPSubClassOfHandler(TripleConsumer consumer) {
        super(consumer);
    }
    
    @Override
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object, Set<Annotation> annotations) {
        ClassExpression subClass=consumer.getClassExpressionForClassIdentifier(subject);
        ClassExpression superClass=consumer.getClassExpressionForClassIdentifier(object);
        String errorMessage="";
        if (subClass==null)
            errorMessage="Could not find a subclass expression for the subject in the triple "+subject+" rdfs:subClassOf "+object+". ";
        if (superClass==null)
            errorMessage+="Could not find a superclass expression for the object in the triple "+subject+" rdfs:subClassOf "+object+". ";    
        if (subClass!=null && superClass!=null)
            consumer.addAxiom(SubClassOf.create(subClass,superClass,annotations));
        else 
            throw new RuntimeException(errorMessage);
    }
}
