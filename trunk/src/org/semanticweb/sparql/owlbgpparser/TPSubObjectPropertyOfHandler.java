package org.semanticweb.sparql.owlbgpparser;

import java.util.List;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.ObjectPropertyChain;
import org.semanticweb.sparql.owlbgp.model.ObjectPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.SubObjectPropertyOf;

public class TPSubObjectPropertyOfHandler extends TriplePredicateHandler {

    public TPSubObjectPropertyOfHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_SUB_OBJECT_PROPERTY_OF.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        // If the subject is anonymous, it *might* be a property chain - we
        // can't handle these in a streaming manner really
        return !consumer.isAnonymousNode(subject);
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        if (consumer.isAnonymousNode(subject) && getConsumer().hasPredicateObject(subject,Vocabulary.RDF_TYPE.getIRI(),Vocabulary.RDF_LIST.getIRI())) {
            // Property chain!
            OptimisedListTranslator<ObjectPropertyExpression> translator=new OptimisedListTranslator<ObjectPropertyExpression>(consumer,new ObjectPropertyExpressionListItemTranslator(consumer));
            List<ObjectPropertyExpression> props=translator.translateList(subject);
            ObjectPropertyExpression chain=ObjectPropertyChain.create(props);
            addAxiom(SubObjectPropertyOf.create(chain,translateObjectProperty(object)));
        } else
            addAxiom(SubObjectPropertyOf.create(translateObjectProperty(subject),translateObjectProperty(object)));
        consumeTriple(subject, predicate, object);
    }
}
