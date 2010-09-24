package org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Annotation;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.AnnotationPropertyDomain;
import org.semanticweb.sparql.owlbgp.model.axioms.DataPropertyDomain;
import org.semanticweb.sparql.owlbgp.model.axioms.ObjectPropertyDomain;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.literals.Literal;
import org.semanticweb.sparql.owlbgp.model.properties.AnnotationPropertyExpression;
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
        ObjectPropertyExpression objectProperty=consumer.getOPE(subject);
        DataPropertyExpression dataProperty=null;
        AnnotationPropertyExpression annotationProperty=null;
        if (objectProperty==null)
            dataProperty=consumer.getDPE(subject);
        if (objectProperty==null && dataProperty==null) {
            annotationProperty=consumer.getAPE(subject);
        }
        if (objectProperty==null && dataProperty==null && annotationProperty==null)
            throw new RuntimeException("Could not find a property expression for the subject in the triple "+subject+" "+predicate+" "+object+". ");
        
        if (annotationProperty!=null) {
            if (consumer.isAnonymous(object) || object instanceof Literal)
                throw new RuntimeException("The object of an annotation property domain triple is anonymous or a literal, which is not allowed:  "+subject+" "+predicate+" "+object+". ");
            consumer.addAxiom(AnnotationPropertyDomain.create(annotationProperty,object,annotations));
        } else {
            ClassExpression classExpression=consumer.getCE(object);
            if (classExpression==null)
                throw new RuntimeException("Could not find a class expression for the object in the triple "+subject+" "+predicate+" "+object+". ");
            if (objectProperty!=null)
                consumer.addAxiom(ObjectPropertyDomain.create(objectProperty,classExpression,annotations));
            else 
                consumer.addAxiom(DataPropertyDomain.create(dataProperty,classExpression,annotations));
        }
    }
}
