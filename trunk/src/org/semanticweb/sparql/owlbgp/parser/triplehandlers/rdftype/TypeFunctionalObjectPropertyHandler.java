package org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.FunctionalObjectProperty;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;

public class TypeFunctionalObjectPropertyHandler extends BuiltInTypeHandler {

    public TypeFunctionalObjectPropertyHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_FUNCTIONAL_OBJECT_PROPERTY.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        return !consumer.isAnonymous(subject);
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        consumer.addAxiom(FunctionalObjectProperty.create(consumer.translateObjectPropertyExpression(subject)));
        consumer.consumeTriple(subject, predicate, object);
    }
}
