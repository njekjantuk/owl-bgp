package org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.ObjectPropertyDomain;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.TriplePredicateHandler;

public class TPObjectPropertyDomainHandler extends TriplePredicateHandler {

    public TPObjectPropertyDomainHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_OBJECT_PROPERTY_DOMAIN.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        // Can only handle when domain is named if we are streaming
        // prop rdfs:domain desc
        return !consumer.isAnonymous(object);
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        ObjectPropertyExpression prop=consumer.translateObjectPropertyExpression(subject);
        ClassExpression domain=consumer.translateClassExpression(subject);
        consumer.addAxiom(ObjectPropertyDomain.create(prop, domain));
    }
}
