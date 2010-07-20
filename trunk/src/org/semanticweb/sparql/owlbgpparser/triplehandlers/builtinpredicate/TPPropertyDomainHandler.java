package org.semanticweb.sparql.owlbgpparser.triplehandlers.builtinpredicate;

import org.semanticweb.sparql.owlbgp.model.IRI;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.AnnotationPropertyDomain;
import org.semanticweb.sparql.owlbgp.model.axioms.DataPropertyDomain;
import org.semanticweb.sparql.owlbgp.model.axioms.ObjectPropertyDomain;
import org.semanticweb.sparql.owlbgp.model.properties.AnnotationPropertyExpression;
import org.semanticweb.sparql.owlbgpparser.TripleConsumer;
import org.semanticweb.sparql.owlbgpparser.Vocabulary;
import org.semanticweb.sparql.owlbgpparser.triplehandlers.TriplePredicateHandler;

public class TPPropertyDomainHandler extends TriplePredicateHandler {

    public TPPropertyDomainHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.RDFS_DOMAIN.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject,Identifier predicate,Identifier object) {
        return false;
    }
    public void handleTriple(Identifier subject,Identifier predicate,Identifier object) {
        if (consumer.isObjectPropertyOnly(subject))
            consumer.addAxiom(ObjectPropertyDomain.create(consumer.translateObjectPropertyExpression(subject),consumer.translateClassExpression(object)));
        else if (consumer.isDataPropertyOnly(subject))
            consumer.addAxiom(DataPropertyDomain.create(consumer.translateDataPropertyExpression(subject),consumer.translateClassExpression(object)));
        else if (consumer.isAnnotationProperty(subject)) {
            AnnotationPropertyExpression prop=consumer.translateAnnotationPropertyExpression(subject);
            consumer.addAxiom(AnnotationPropertyDomain.create(prop, (IRI)object, consumer.getPendingAnnotations()));
        } else {
            // See if there are any range triples that we can peek at
            Identifier rangeIRI=consumer.getResourceObject(subject, predicate, false);
            if (consumer.isDataRange(rangeIRI)) 
                consumer.addAxiom(DataPropertyDomain.create(consumer.translateDataPropertyExpression(subject),consumer.translateClassExpression(object)));
            else throw new RuntimeException("Could not disambiguate property "+subject+". "); 
        }
        consumer.consumeTriple(subject, predicate, object);
    }
}
