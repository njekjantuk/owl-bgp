package org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Annotation;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.DatatypeDefinition;
import org.semanticweb.sparql.owlbgp.model.axioms.EquivalentClasses;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.dataranges.DataRange;
import org.semanticweb.sparql.owlbgp.model.dataranges.Datatype;
import org.semanticweb.sparql.owlbgp.model.dataranges.DatatypeVariable;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.TriplePredicateHandler;

public class TPEquivalentClassHandler extends TriplePredicateHandler {

    public TPEquivalentClassHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_EQUIVALENT_CLASS);
    }
    
    @Override
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object, Set<Annotation> annotations) {
        boolean handled=false;
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
            consumer.addAxiom(EquivalentClasses.create(classes,annotations));
            handled=true;
        } else if (class1==null && class2==null) {
            // data ranges
            DataRange dr1=consumer.getDataRangeForDataRangeIdentifier(subject);
            DataRange dr2=consumer.getDataRangeForDataRangeIdentifier(object);
            if (dr1==null)
                errorMessage+="Could not find a data range for the subject in the triple "+subject+" owl:equivalentClass "+object+". ";
            if (dr2==null)
                errorMessage+="Could not find a data range for the object in the triple "+subject+" owl:equivalentClass "+object+". ";
            if (dr1!=null && dr2!=null) {
                handled=true;
                if (dr1 instanceof Datatype) 
                    consumer.addAxiom(DatatypeDefinition.create((Datatype)dr1, dr2,annotations));
                else if (dr1 instanceof DatatypeVariable)
                    consumer.addAxiom(DatatypeDefinition.create((DatatypeVariable)dr1, dr2,annotations));
                else {
                    errorMessage="The data range for the subject in the triple "+subject+" owl:equivalentClass "+object+" is not a datatype or a datatype variable as required, but a complex data range: "+dr1;
                    handled=false;
                }
            }
        } 
        if (!handled) throw new RuntimeException(errorMessage);
    }
}
