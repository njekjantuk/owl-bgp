package org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.FunctionalDataProperty;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;

public class TypeFunctionalDataPropertyHandler extends BuiltInTypeHandler {

    public TypeFunctionalDataPropertyHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_FUNCTIONAL_DATA_PROPERTY.getIRI());
    }

    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        consumer.addAxiom(FunctionalDataProperty.create(consumer.translateDataPropertyExpression(subject)));
        consumer.consumeTriple(subject, predicate, object);
    }
}
