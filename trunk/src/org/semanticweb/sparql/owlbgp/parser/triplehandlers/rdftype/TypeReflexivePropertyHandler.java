package org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.ReflexiveObjectProperty;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;

public class TypeReflexivePropertyHandler extends BuiltInTypeHandler {

    public TypeReflexivePropertyHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_REFLEXIVE_PROPERTY.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        return !consumer.isAnonymous(subject);
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        consumer.addAxiom(ReflexiveObjectProperty.create(consumer.translateObjectPropertyExpression(subject)));
        consumer.consumeTriple(subject, predicate, object);
    }
}
