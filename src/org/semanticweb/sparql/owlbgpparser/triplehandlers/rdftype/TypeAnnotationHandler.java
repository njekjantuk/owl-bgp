package org.semanticweb.sparql.owlbgpparser.triplehandlers.rdftype;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgpparser.TripleConsumer;
import org.semanticweb.sparql.owlbgpparser.Vocabulary;

public class TypeAnnotationHandler extends BuiltInTypeHandler {

    public TypeAnnotationHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_ANNOTATION.getIRI());
    }
    
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        consumer.addAnnotationIRI(subject);
    }
}
