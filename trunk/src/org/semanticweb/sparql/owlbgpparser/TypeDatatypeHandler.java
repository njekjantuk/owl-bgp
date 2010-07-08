package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Datatype;

public class TypeDatatypeHandler extends BuiltInTypeHandler {

    public TypeDatatypeHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.RDFS_DATATYPE.getIRI());
    }

    public void handleTriple(String subject, String predicate, String object) {
        if (consumer.isVariableNode(subject)) {
            consumer.datatypeVars.add(subject);
        } else if (!consumer.isAnonymousNode(subject)) {
            Datatype dt=Datatype.create(subject);
            if (!dt.isOWL2Datatype()) {
                consumer.addOWLDatatype(subject);
            }
        }
    }
}
