package org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.FunctionalDataProperty;
import org.semanticweb.sparql.owlbgp.model.axioms.FunctionalObjectProperty;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;

public class TypeFunctionalPropertyHandler extends BuiltInTypeHandler {

    public TypeFunctionalPropertyHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_FUNCTIONAL_PROPERTY.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        return consumer.isObjectPropertyOnly(subject) || consumer.isDataPropertyOnly(subject);
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        if (consumer.isObjectPropertyOnly(subject))
            consumer.addAxiom(FunctionalObjectProperty.create(consumer.translateObjectPropertyExpression(subject)));
        else if (consumer.isDataPropertyOnly(subject))
            consumer.addAxiom(FunctionalDataProperty.create(consumer.translateDataPropertyExpression(subject)));
        else
            throw new IllegalArgumentException("Property "+subject+" cannot be identified as object or data property (variable). ");
        consumer.consumeTriple(subject, predicate, object);
    }
}
