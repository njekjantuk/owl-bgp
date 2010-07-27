package org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import org.semanticweb.sparql.owlbgp.model.IRI;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.ClassAssertion;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.TriplePredicateHandler;

public class TPTypeHandler extends TriplePredicateHandler {

    public TPTypeHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.RDF_TYPE.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        // Can handle if object isn't anonymous and either the object
        // is owl:Thing, or it is not part of the build in vocabulary
        return !consumer.isAnonymous(object) &&
                (object.equals(Vocabulary.OWL_THING.getIRI()) 
                        || (object instanceof IRI && !Vocabulary.BUILT_IN_VOCABULARY_IRIS.contains((IRI)object)));
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        if (Vocabulary.BUILT_IN_VOCABULARY_IRIS.contains(object) && !((IRI)object).equals(Vocabulary.OWL_THING.getIRI()))
            throw new RuntimeException("Error: individual of builtin type " + object); // Can't have instance of built in vocabulary!
        consumer.addAxiom(ClassAssertion.create(consumer.translateClassExpression(object),consumer.translateIndividual(subject)));
        consumer.consumeTriple(subject, predicate, object);
    }
}
