package org.semanticweb.sparql.owlbgpparser.triplehandlers.builtinpredicate;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ObjectOneOf;
import org.semanticweb.sparql.owlbgpparser.TripleConsumer;
import org.semanticweb.sparql.owlbgpparser.Vocabulary;

public class TPOneOfHandler extends AbstractNamedEquivalentClassAxiomHandler {

    public TPOneOfHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_ONE_OF.getIRI());
    }
    protected ClassExpression translateEquivalentClass(Identifier mainNode) {
    	return ObjectOneOf.create(consumer.translateToIndividualSet(mainNode));
    }
}
