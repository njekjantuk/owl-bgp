package org.semanticweb.sparql.owlbgpparser;

public class TypeIndividualVariableHandler extends BuiltInTypeHandler {

    public TypeIndividualVariableHandler(OWLRDFConsumer consumer) {
        super(consumer, OWLRDFVocabulary.OWL_NAMED_INDIVIDUAL.getIRI());
    }
    
    public void handleTriple(String subject, String predicate, String object) {
        if (consumer.isVariableNode(subject)) consumer.individualVars.add(subject);
        else consumer.individualIRIs.add(subject);
    }
}