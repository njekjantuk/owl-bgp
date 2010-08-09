package org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Annotation;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.DataPropertyRange;
import org.semanticweb.sparql.owlbgp.model.axioms.ObjectPropertyRange;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.dataranges.DataRange;
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
        ObjectPropertyExpression property=consumer.getObjectPropertyExpressionForObjectPropertyIdentifier(subject);
        DataPropertyExpression dataProperty=null;
        if (property==null)
            dataProperty=consumer.getDataPropertyExpressionForDataPropertyIdentifier(subject);
        if (property==null && dataProperty==null)
            throw new RuntimeException("Could not find a property expression for the subject in the triple "+subject+" "+predicate+" "+object+". ");
        
        if (property!=null) {
            ClassExpression classExpression=consumer.getClassExpressionForClassIdentifier(object);
            if (classExpression==null)
                throw new RuntimeException("Could not find a class expression for the object in the triple "+subject+" "+predicate+" "+object+". ");
            consumer.addAxiom(ObjectPropertyRange.create(property,classExpression,annotations));
        } else {
            DataRange dataRange=consumer.getDataRangeForDataRangeIdentifier(object);
            if (dataRange==null)
                throw new RuntimeException("Could not find a data range for the object in the triple "+subject+" "+predicate+" "+object+". ");
            consumer.addAxiom(DataPropertyRange.create(dataProperty,dataRange,annotations));
        }
    }
}
