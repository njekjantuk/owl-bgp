package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.IrreflexiveObjectProperty;

public class TypeIrreflexivePropertyHandler extends BuiltInTypeHandler {

    public TypeIrreflexivePropertyHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_IRREFLEXIVE_PROPERTY.getIRI());
    }
    
    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        return !consumer.isAnonymousNode(subject);
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        addAxiom(IrreflexiveObjectProperty.create(translateObjectProperty(subject)));
        consumeTriple(subject, predicate, object);
    }
}
