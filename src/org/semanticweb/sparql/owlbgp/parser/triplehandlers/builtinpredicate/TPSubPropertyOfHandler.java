package org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Annotation;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.SubDataPropertyOf;
import org.semanticweb.sparql.owlbgp.model.axioms.SubObjectPropertyOf;
import org.semanticweb.sparql.owlbgp.model.properties.DataPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.AbstractResourceTripleHandler;

public class TPSubPropertyOfHandler extends AbstractResourceTripleHandler {

    public TPSubPropertyOfHandler(TripleConsumer consumer) {
        super(consumer);
    }

    @Override
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object, Set<Annotation> annotations) {
        ObjectPropertyExpression subProperty=consumer.getObjectPropertyExpressionForObjectPropertyIdentifier(subject);
        DataPropertyExpression subDataProperty=null;
        if (subProperty==null) {
            subDataProperty=consumer.getDataPropertyExpressionForDataPropertyIdentifier(subject);
            throw new RuntimeException("Could not find a property expression for the subject in the triple "+subject+" "+predicate+" "+object+". ");
        }
        
        ObjectPropertyExpression superProperty=consumer.getObjectPropertyExpressionForObjectPropertyIdentifier(object);
        DataPropertyExpression superDataProperty=null;
        if (superProperty==null) {
            superDataProperty=consumer.getDataPropertyExpressionForDataPropertyIdentifier(object);
            throw new RuntimeException("Could not find a property expression for the object in the triple "+subject+" "+predicate+" "+object+". ");
        }    
            
        if (subProperty!=null && superProperty!=null)
            consumer.addAxiom(SubObjectPropertyOf.create(subProperty,superProperty,annotations));
        else if (subDataProperty!=null && superDataProperty!=null)
            consumer.addAxiom(SubDataPropertyOf.create(subDataProperty,superDataProperty,annotations));
        else if (subProperty!=null)
            throw new RuntimeException("Error: The subject of the triple "+subject+" "+predicate+" "+object+"  was tranlated into an object property, but the object into a data property, which is not allowed in OWL DL. ");
        else 
            throw new RuntimeException("Error: The object of the triple "+subject+" "+predicate+" "+object+"  was tranlated into an object property, but the subject into a data property, which is not allowed in OWL DL. ");
    }
}
