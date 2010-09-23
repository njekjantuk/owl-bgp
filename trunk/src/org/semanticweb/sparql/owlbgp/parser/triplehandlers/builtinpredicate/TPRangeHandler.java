package org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Annotation;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.AnnotationPropertyRange;
import org.semanticweb.sparql.owlbgp.model.axioms.DataPropertyRange;
import org.semanticweb.sparql.owlbgp.model.axioms.ObjectPropertyRange;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.dataranges.DataRange;
import org.semanticweb.sparql.owlbgp.model.properties.AnnotationPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.properties.DataPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.TripleHandler;

public class TPRangeHandler extends TripleHandler {

    public TPRangeHandler(TripleConsumer consumer) {
        super(consumer);
    }
    
    @Override
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object, Set<Annotation> annotations) {    
        ObjectPropertyExpression objectProperty=consumer.getObjectPropertyExpressionForObjectPropertyIdentifier(subject);
        DataPropertyExpression dataProperty=null;
        AnnotationPropertyExpression annotationProperty=null;
        if (objectProperty==null)
            dataProperty=consumer.getDataPropertyExpressionForDataPropertyIdentifier(subject);
        if (objectProperty==null && dataProperty==null)
            annotationProperty=consumer.getAnnotationPropertyExpressionForAnnotationPropertyIdentifier(subject);
        if (objectProperty==null && dataProperty==null && annotationProperty==null)
            throw new RuntimeException("Could not find a property expression for the subject in the triple "+subject+" "+predicate+" "+object+". ");
        
        if (objectProperty!=null) {
            ClassExpression classExpression=consumer.getClassExpressionForClassIdentifier(object);
            if (classExpression==null)
                throw new RuntimeException("Could not find a class expression for the object in the triple "+subject+" "+predicate+" "+object+". ");
            consumer.addAxiom(ObjectPropertyRange.create(objectProperty,classExpression,annotations));
        } else if (dataProperty!=null) {
            DataRange dataRange=consumer.getDataRangeForDataRangeIdentifier(object);
            if (dataRange==null)
                throw new RuntimeException("Could not find a data range for the object in the triple "+subject+" "+predicate+" "+object+". ");
            consumer.addAxiom(DataPropertyRange.create(dataProperty,dataRange,annotations));
        } else {
            if (consumer.isAnonymous(object))
                throw new RuntimeException("The range of an annotation property is annonymous, which is not allowed. Triple "+subject+" "+predicate+" "+object+". ");
            consumer.addAxiom(AnnotationPropertyRange.create(annotationProperty,object,annotations));
        }
    }
}
