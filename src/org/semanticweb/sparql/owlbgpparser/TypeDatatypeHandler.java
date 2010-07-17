package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.Datatype;
import org.semanticweb.sparql.owlbgp.model.Declaration;
import org.semanticweb.sparql.owlbgp.model.Identifier;

public class TypeDatatypeHandler extends BuiltInTypeHandler {

    public TypeDatatypeHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.RDFS_DATATYPE.getIRI());
    }

    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        if (!consumer.isAnonymousNode(subject)) {
            Datatype dt=(Datatype)consumer.translateDataRange(subject);
            if (!dt.isOWL2Datatype()) addAxiom(Declaration.create((Atomic)dt, consumer.getPendingAnnotations()));
            consumer.addDataRange(subject);
        }
    }
}
