package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.IrreflexiveObjectProperty;

public class TypeIrreflexivePropertyHandler extends BuiltInTypeHandler {

    public TypeIrreflexivePropertyHandler(OWLRDFConsumer consumer) {
        super(consumer, OWLRDFVocabulary.OWL_IRREFLEXIVE_PROPERTY.getIRI());
    }
    
    public boolean canHandleStreaming(String subject, String predicate, String object) {
        consumer.addOWLObjectProperty(subject);
        return !consumer.isAnonymousNode(subject);
    }
    public void handleTriple(String subject, String predicate, String object) {
        consumer.addOWLObjectProperty(subject);
        addAxiom(IrreflexiveObjectProperty.create(translateObjectProperty(subject)));
        consumeTriple(subject, predicate, object);
    }
}
