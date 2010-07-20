package org.semanticweb.sparql.owlbgpparser.triplehandlers.builtinpredicate;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ObjectComplementOf;
import org.semanticweb.sparql.owlbgpparser.TripleConsumer;
import org.semanticweb.sparql.owlbgpparser.Vocabulary;

public class TPComplementOfHandler extends AbstractNamedEquivalentClassAxiomHandler {

    public TPComplementOfHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_COMPLEMENT_OF.getIRI());
    }

    protected ClassExpression translateEquivalentClass(Identifier mainNode) {
        return ObjectComplementOf.create(consumer.translateClassExpression(mainNode));
    }
}
