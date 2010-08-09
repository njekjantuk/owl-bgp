package org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Annotation;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.EquivalentDataProperties;
import org.semanticweb.sparql.owlbgp.model.axioms.EquivalentObjectProperties;
import org.semanticweb.sparql.owlbgp.model.properties.DataPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.AbstractResourceTripleHandler;

public class TPEquivalentPropertyHandler extends AbstractResourceTripleHandler {

    public TPEquivalentPropertyHandler(TripleConsumer consumer) {
        super(consumer);
    }
    
    @Override
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object, Set<Annotation> annotations) {    
        ObjectPropertyExpression property1=consumer.getObjectPropertyExpressionForObjectPropertyIdentifier(subject);
        DataPropertyExpression dataProperty1=null;
        if (property1==null)
            dataProperty1=consumer.getDataPropertyExpressionForDataPropertyIdentifier(subject);
        if (property1==null && dataProperty1==null)
            throw new RuntimeException("Could not find a property expression for the subject in the triple "+subject+" "+predicate+" "+object+". ");
        
        ObjectPropertyExpression property2=consumer.getObjectPropertyExpressionForObjectPropertyIdentifier(object);
        DataPropertyExpression dataProperty2=null;
        if (property2==null)
            dataProperty2=consumer.getDataPropertyExpressionForDataPropertyIdentifier(object);
        if (property1==null && dataProperty1==null)
            throw new RuntimeException("Could not find a property expression for the object in the triple "+subject+" "+predicate+" "+object+". ");
        
        if (property1!=null && property2!=null)
            consumer.addAxiom(EquivalentObjectProperties.create(property1,property2,annotations));
        else if (dataProperty1!=null && dataProperty2!=null)
            consumer.addAxiom(EquivalentDataProperties.create(dataProperty1,dataProperty2,annotations));
        else if (property1!=null)
            throw new RuntimeException("Error: The subject of the triple "+subject+" "+predicate+" "+object+"  was tranlated into an object property, but the object into a data property, which is not allowed in OWL DL. ");
        else 
            throw new RuntimeException("Error: The object of the triple "+subject+" "+predicate+" "+object+"  was tranlated into an object property, but the subject into a data property, which is not allowed in OWL DL. ");
    }
}
