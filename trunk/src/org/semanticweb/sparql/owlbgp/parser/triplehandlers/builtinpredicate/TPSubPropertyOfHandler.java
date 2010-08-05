package org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Annotation;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.SubDataPropertyOf;
import org.semanticweb.sparql.owlbgp.model.axioms.SubObjectPropertyOf;
import org.semanticweb.sparql.owlbgp.model.properties.DataPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.TriplePredicateHandler;

public class TPSubPropertyOfHandler extends TriplePredicateHandler {

    public TPSubPropertyOfHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.RDFS_SUB_PROPERTY_OF);
    }

    @Override
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object, Set<Annotation> annotations) {
        boolean handled=false;
        ObjectPropertyExpression subProperty=consumer.getObjectPropertyExpressionForObjectPropertyIdentifier(subject);
        ObjectPropertyExpression superProperty=consumer.getObjectPropertyExpressionForObjectPropertyIdentifier(object);
        String errorMessage="";
        if (subProperty==null)
            errorMessage="Could not find a subclass expression for the subject in the triple "+subject+" rdfs:subClassOf "+object+". ";
        if (superProperty==null)
            errorMessage+="Could not find a superclass expression for the object in the triple "+subject+" rdfs:subClassOf "+object+". ";    
        if (subProperty!=null && superProperty!=null) {
            consumer.addAxiom(SubObjectPropertyOf.create(subProperty,superProperty,annotations));
            handled=true;
        } else if (subProperty==null&&superProperty==null) {
            DataPropertyExpression subDataProperty=consumer.getDataPropertyExpressionForDataPropertyIdentifier(subject);
            DataPropertyExpression superDataProperty=consumer.getDataPropertyExpressionForDataPropertyIdentifier(object);
            if (subDataProperty==null)
                errorMessage+="Could not find a subclass expression for the subject in the triple "+subject+" rdfs:subClassOf "+object+". ";
            if (superDataProperty==null)
                errorMessage+="Could not find a superclass expression for the object in the triple "+subject+" rdfs:subClassOf "+object+". ";    
            if (subDataProperty!=null && superDataProperty!=null) {
                consumer.addAxiom(SubDataPropertyOf.create(subDataProperty,superDataProperty,annotations));
                handled=true;
            }
        }
        if (!handled)
            throw new RuntimeException(errorMessage);
    }
}
