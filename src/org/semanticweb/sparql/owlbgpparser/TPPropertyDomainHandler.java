package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.AnnotationPropertyDomain;
import org.semanticweb.sparql.owlbgp.model.AnnotationPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.DataPropertyDomain;
import org.semanticweb.sparql.owlbgp.model.IRI;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.ObjectPropertyDomain;

public class TPPropertyDomainHandler extends TriplePredicateHandler {

    public TPPropertyDomainHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.RDFS_DOMAIN.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject,Identifier predicate,Identifier object) {
        return false;
    }
    public void handleTriple(Identifier subject,Identifier predicate,Identifier object) {
        if (consumer.isObjectPropertyOnly(subject))
            translateObjectPropertyDomain(subject, predicate, object);
        else if (consumer.isDataPropertyOnly(subject))
            translateDataPropertyDomain(subject, predicate, object);
        else if (consumer.isAnnotationProperty(subject)) {
            AnnotationPropertyExpression prop=consumer.translateAnnotationPropertyExpression(subject);
            consumer.addAxiom(AnnotationPropertyDomain.create(prop, (IRI)object, consumer.getPendingAnnotations()));
            consumeTriple(subject, predicate, object);
        } else {
            // See if there are any range triples that we can peek at
            Identifier rangeIRI=consumer.getResourceObject(subject, predicate, false);
            if (consumer.isDataRange(rangeIRI)) translateDataPropertyDomain(subject, predicate, object);
            else throw new RuntimeException("Could not disambiguate property "+subject+". "); 
        }
    }
    protected void translateDataPropertyDomain(Identifier subject, Identifier predicate, Identifier object) {
        addAxiom(DataPropertyDomain.create(translateDataProperty(subject), translateClassExpression(object)));
        consumeTriple(subject, predicate, object);
    }
    protected void translateObjectPropertyDomain(Identifier subject,Identifier predicate,Identifier object) {
        addAxiom(ObjectPropertyDomain.create(translateObjectProperty(subject), translateClassExpression(object)));
        consumeTriple(subject, predicate, object);
    }
}
