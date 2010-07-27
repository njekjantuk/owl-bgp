package org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Annotation;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.DataPropertyDomain;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.TriplePredicateHandler;

public class TPDataPropertDomainHandler extends TriplePredicateHandler {

    public TPDataPropertDomainHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_DATA_PROPERTY_DOMAIN.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        return false;
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        Set<Annotation> annos=consumer.getAnnotations(subject, predicate, object, (Identifier)Vocabulary.OWL_AXIOM.getIRI());
        consumer.addAxiom(DataPropertyDomain.create(consumer.translateDataPropertyExpression(subject),consumer.translateClassExpression(object),annos));
        consumer.consumeTriple(subject, predicate, object);
    }
}
