package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.DataPropertyDomain;
import org.semanticweb.sparql.owlbgp.model.ObjectPropertyDomain;

public class TPPropertyDomainHandler extends TriplePredicateHandler {

    public TPPropertyDomainHandler(OWLRDFConsumer consumer) {
        super(consumer, OWLRDFVocabulary.RDFS_DOMAIN.getIRI());
    }

    public boolean canHandleStreaming(String subject,String predicate,String object) {
        return false;
    }
    public void handleTriple(String subject,String predicate,String object) {
        if (consumer.isObjectPropertyOnly(subject))
            translateObjectPropertyDomain(subject, predicate, object);
        else if (consumer.isDataPropertyOnly(subject))
            translateDataPropertyDomain(subject, predicate, object);
        else if (consumer.isAnnotationProperty(subject)) {
            consumer.hasAnnotations=true;
            consumeTriple(subject, predicate, object);
        } else {
            // See if there are any range triples that we can peek at
            String rangeIRI=consumer.getResourceObject(subject, predicate, false);
            if (consumer.isDataRange(rangeIRI))
                translateDataPropertyDomain(subject, predicate, object);
            else throw new RuntimeException("Could not disambiguate property "+subject+". "); 
        }
    }
    protected void translateDataPropertyDomain(String subject, String predicate, String object) {
        addAxiom(DataPropertyDomain.create(translateDataProperty(subject), translateClassExpression(object)));
        consumeTriple(subject, predicate, object);
    }
    protected void translateObjectPropertyDomain(String subject,String predicate,String object) {
        addAxiom(ObjectPropertyDomain.create(translateObjectProperty(subject), translateClassExpression(object)));
        consumeTriple(subject, predicate, object);
    }
}
