package org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.Import;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.TriplePredicateHandler;

public class TPImportsHandler extends TriplePredicateHandler {

    public TPImportsHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_IMPORTS);
    }

    @Override
    public void handleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        super.handleStreaming(subject, predicate, object, true);
        if (consumer.isAnonymous(object)) throw new RuntimeException("Error: Import triples should not have a blank node as object, but here we have: "+subject+" "+predicate+" "+object);  
        else consumer.addImport(Import.create(object));
    }
}
