package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.ObjectPropertyDomain;
import org.semanticweb.sparql.owlbgp.model.ObjectPropertyExpression;

public class TPObjectPropertyDomainHandler extends TriplePredicateHandler {

    public TPObjectPropertyDomainHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_OBJECT_PROPERTY_DOMAIN.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        // Can only handle when domain is named if we are streaming
        // prop rdfs:domain desc
        return !consumer.isAnonymousNode(object);
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        ObjectPropertyExpression prop=translateObjectProperty(subject);
        ClassExpression domain=translateClassExpression(subject);
        addAxiom(ObjectPropertyDomain.create(prop, domain));
    }
}
