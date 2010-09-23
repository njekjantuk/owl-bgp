package org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Annotation;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.FunctionalDataProperty;
import org.semanticweb.sparql.owlbgp.model.axioms.FunctionalObjectProperty;
import org.semanticweb.sparql.owlbgp.model.properties.DataPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.TripleHandler;

public class FunctionalPropertyHandler extends TripleHandler {

    public FunctionalPropertyHandler(TripleConsumer consumer) {
        super(consumer);
    }

    @Override
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object, Set<Annotation> annotations) {
        ObjectPropertyExpression ope=consumer.getObjectPropertyExpressionForObjectPropertyIdentifier(subject);
        if (ope!=null)
            consumer.addAxiom(FunctionalObjectProperty.create(ope,annotations));
        else {
            DataPropertyExpression dpe=consumer.getDataPropertyExpressionForDataPropertyIdentifier(subject);
            if (dpe!=null)
                consumer.addAxiom(FunctionalDataProperty.create(dpe,annotations));
            else 
                throw new RuntimeException("Error: The subject of the triple "+subject+" "+predicate+" "+object+" could not be translated into a data or object property. Maybe a declaration is missing for "+subject+"?");
        }
    }
}
