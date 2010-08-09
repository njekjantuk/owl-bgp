package org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Annotation;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.DataPropertyDomain;
import org.semanticweb.sparql.owlbgp.model.axioms.ObjectPropertyDomain;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.properties.DataPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.TripleHandler;

public class TPDomainHandler extends TripleHandler {

    public TPDomainHandler(TripleConsumer consumer) {
        super(consumer);
    }
    
    @Override
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object, Set<Annotation> annotations) {    
        ObjectPropertyExpression property=consumer.getObjectPropertyExpressionForObjectPropertyIdentifier(subject);
        DataPropertyExpression dataProperty=null;
        if (property==null)
            dataProperty=consumer.getDataPropertyExpressionForDataPropertyIdentifier(subject);
        if (property==null && dataProperty==null)
            throw new RuntimeException("Could not find a property expression for the subject in the triple "+subject+" "+predicate+" "+object+". ");
        
        ClassExpression classExpression=consumer.getClassExpressionForClassIdentifier(object);
        if (classExpression==null)
            throw new RuntimeException("Could not find a class expression for the object in the triple "+subject+" "+predicate+" "+object+". ");
        
        if (property!=null)
            consumer.addAxiom(ObjectPropertyDomain.create(property,classExpression,annotations));
        else 
            consumer.addAxiom(DataPropertyDomain.create(dataProperty,classExpression,annotations));
    }
}
