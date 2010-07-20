package org.semanticweb.sparql.owlbgpparser.triplehandlers.builtinpredicate;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ObjectUnionOf;
import org.semanticweb.sparql.owlbgpparser.TripleConsumer;
import org.semanticweb.sparql.owlbgpparser.Vocabulary;

public class TPUnionOfHandler extends AbstractNamedEquivalentClassAxiomHandler {

    public TPUnionOfHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_UNION_OF.getIRI());
    }
    
    protected ClassExpression translateEquivalentClass(Identifier mainNode) {
        return ObjectUnionOf.create(consumer.translateToClassExpressionSet(mainNode));
    }
}
