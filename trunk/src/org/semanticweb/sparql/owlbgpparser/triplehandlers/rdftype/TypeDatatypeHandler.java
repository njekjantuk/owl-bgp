package org.semanticweb.sparql.owlbgpparser.triplehandlers.rdftype;

import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.Declaration;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.Variable;
import org.semanticweb.sparql.owlbgp.model.dataranges.DataRange;
import org.semanticweb.sparql.owlbgp.model.dataranges.Datatype;
import org.semanticweb.sparql.owlbgpparser.TripleConsumer;
import org.semanticweb.sparql.owlbgpparser.Vocabulary;

public class TypeDatatypeHandler extends BuiltInTypeHandler {

    public TypeDatatypeHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.RDFS_DATATYPE.getIRI());
    }

    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        if (!consumer.isAnonymous(subject)) {
            DataRange dt=consumer.translateDataRange(subject);
            if (dt instanceof Variable || !((Datatype)dt).isOWL2Datatype()) consumer.addAxiom(Declaration.create((Atomic)dt, consumer.getPendingAnnotations()));
        }
        consumer.addDataRange(subject);
    }
}
