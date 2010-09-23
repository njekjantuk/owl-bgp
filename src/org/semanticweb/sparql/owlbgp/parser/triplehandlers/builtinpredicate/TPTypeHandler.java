package org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Annotation;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.ClassAssertion;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.individuals.Individual;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.TripleHandler;

public class TPTypeHandler extends TripleHandler {

    public TPTypeHandler(TripleConsumer consumer) {
        super(consumer);
    }
    
    @Override
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object, Set<Annotation> annotations) {
        ClassExpression classexpression=consumer.getClassExpressionForClassIdentifier(object);
        Individual individual=consumer.getIndividualForIndividualIdentifier(subject);
        String errorMessage="";
        if (individual==null)
            errorMessage="Could not find an individual for the subject in the triple "+subject+" "+predicate+" "+object+". ";
        if (classexpression==null)
            errorMessage+="Could not find a class expression for the object in the triple "+subject+" "+predicate+" "+object+". ";    
        if (individual!=null && classexpression!=null)
            consumer.addAxiom(ClassAssertion.create(classexpression,individual,annotations));
        else 
            throw new RuntimeException(errorMessage);
    }
}
