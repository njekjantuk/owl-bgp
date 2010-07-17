package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Identifier;

public class TypeAnnotationHandler extends BuiltInTypeHandler {

    public TypeAnnotationHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_ANNOTATION.getIRI());
    }
    
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        consumer.addAnnotationIRI(subject);
    }
}
