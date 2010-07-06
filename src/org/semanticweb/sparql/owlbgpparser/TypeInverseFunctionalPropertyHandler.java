package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.InverseFunctionalObjectProperty;

public class TypeInverseFunctionalPropertyHandler extends BuiltInTypeHandler {

    public TypeInverseFunctionalPropertyHandler(OWLRDFConsumer consumer) {
        super(consumer, OWLRDFVocabulary.OWL_INVERSE_FUNCTIONAL_PROPERTY.getIRI());
    }
    
    public boolean canHandleStreaming(String subject, String predicate, String object) {
        consumer.addOWLObjectProperty(subject);
        return !consumer.isAnonymousNode(subject);
    }
    public void handleTriple(String subject, String predicate, String object) {
        consumer.addOWLObjectProperty(subject);
        addAxiom(InverseFunctionalObjectProperty.create(translateObjectProperty(subject)));
        consumeTriple(subject, predicate, object);
    }
}
