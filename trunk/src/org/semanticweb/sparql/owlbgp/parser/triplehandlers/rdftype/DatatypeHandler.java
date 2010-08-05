package org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Annotation;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.Declaration;
import org.semanticweb.sparql.owlbgp.model.dataranges.Datatype;
import org.semanticweb.sparql.owlbgp.model.dataranges.DatatypeVariable;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;

public class DatatypeHandler extends BuiltInTypeHandler {

    public DatatypeHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.RDFS_DATATYPE);
    }

    @Override
    public void handleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        super.handleStreaming(subject, predicate, object, false);
        if (consumer.isVariable(subject))
            consumer.mapDataRangeIdentifierToDataRange(subject, DatatypeVariable.create(subject.toString()));
        else if (!consumer.isAnonymous(subject))
            consumer.mapDataRangeIdentifierToDataRange(subject, Datatype.create(subject.toString()));
    }
    @Override
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object, Set<Annotation> annotations) {
        if (consumer.isVariable(subject))
            consumer.addAxiom(Declaration.create((DatatypeVariable)consumer.getDataRangeForDataRangeIdentifier(subject),annotations));
        else 
            consumer.addAxiom(Declaration.create((Datatype)consumer.getDataRangeForDataRangeIdentifier(subject),annotations)); 
    }
}
