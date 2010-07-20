package org.semanticweb.sparql.owlbgpparser.triplehandlers.builtinpredicate;

import java.util.List;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.SubObjectPropertyOf;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyChain;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;
import org.semanticweb.sparql.owlbgpparser.TripleConsumer;
import org.semanticweb.sparql.owlbgpparser.Vocabulary;
import org.semanticweb.sparql.owlbgpparser.translators.ObjectPropertyExpressionListItemTranslator;
import org.semanticweb.sparql.owlbgpparser.translators.OptimisedListTranslator;
import org.semanticweb.sparql.owlbgpparser.triplehandlers.TriplePredicateHandler;

public class TPSubObjectPropertyOfHandler extends TriplePredicateHandler {

    public TPSubObjectPropertyOfHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_SUB_OBJECT_PROPERTY_OF.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        // If the subject is anonymous, it *might* be a property chain - we
        // can't handle these in a streaming manner really
        return !consumer.isAnonymous(subject);
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        if (consumer.isAnonymous(subject) && consumer.hasPredicateObject(subject,Vocabulary.RDF_TYPE.getIRI(),Vocabulary.RDF_LIST.getIRI())) {
            // Property chain!
            OptimisedListTranslator<ObjectPropertyExpression> translator=new OptimisedListTranslator<ObjectPropertyExpression>(consumer,new ObjectPropertyExpressionListItemTranslator(consumer));
            List<ObjectPropertyExpression> props=translator.translateList(subject);
            ObjectPropertyExpression chain=ObjectPropertyChain.create(props);
            consumer.addAxiom(SubObjectPropertyOf.create(chain,consumer.translateObjectPropertyExpression(object)));
        } else
            consumer.addAxiom(SubObjectPropertyOf.create(consumer.translateObjectPropertyExpression(subject),consumer.translateObjectPropertyExpression(object)));
        consumer.consumeTriple(subject, predicate, object);
    }
}
