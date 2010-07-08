package org.semanticweb.sparql.owlbgpparser;

public class TPAllValuesFromHandler extends TriplePredicateHandler {

    public TPAllValuesFromHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_ALL_VALUES_FROM.getIRI());
    }

    public boolean canHandleStreaming(String subject, String predicate, String object) {
        String propIRI = consumer.getResourceObject(subject, Vocabulary.OWL_ON_PROPERTY.getIRI(), false);
        if (propIRI!=null && (!consumer.isAnonymousNode(object) || consumer.getClassExpressionIfTranslated(object) != null)) {
            // The filler is either a datatype or named class
            if (consumer.isObjectPropertyOnly(propIRI)) {
                consumer.addOWLClass(object);
                consumer.addTriple(subject, predicate, object);
                consumer.translateClassExpression(subject);
                return true;
            }
        }
        return false;
    }
    public void handleTriple(String subject, String predicate, String object) {
    }
}
