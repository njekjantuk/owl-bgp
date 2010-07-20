package org.semanticweb.sparql.owlbgpparser.triplehandlers.rdftype;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.InverseFunctionalObjectProperty;
import org.semanticweb.sparql.owlbgpparser.TripleConsumer;
import org.semanticweb.sparql.owlbgpparser.Vocabulary;

public class TypeInverseFunctionalPropertyHandler extends BuiltInTypeHandler {

    public TypeInverseFunctionalPropertyHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_INVERSE_FUNCTIONAL_PROPERTY.getIRI());
    }
    
    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        return !consumer.isAnonymous(subject);
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        consumer.addAxiom(InverseFunctionalObjectProperty.create(consumer.translateObjectPropertyExpression(subject)));
        consumer.consumeTriple(subject, predicate, object);
    }
}
