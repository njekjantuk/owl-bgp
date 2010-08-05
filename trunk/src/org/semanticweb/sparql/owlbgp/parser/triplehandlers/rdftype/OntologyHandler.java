package org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;

public class OntologyHandler extends BuiltInTypeHandler {

    public OntologyHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_ONTOLOGY);
    }

    @Override
    public void handleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        super.handleStreaming(subject, predicate, object, true);
        consumer.setOntologyIRI(subject);
    }
}
