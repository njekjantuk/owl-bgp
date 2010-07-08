package org.semanticweb.sparql.owlbgpparser;

public class TypeDataPropertyHandler extends BuiltInTypeHandler {

    public TypeDataPropertyHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_DATA_PROPERTY.getIRI());
    }

    public void handleTriple(String subject, String predicate, String object) {
        if (consumer.isVariableNode(subject)) consumer.dataPropertyVars.add(subject);
        else if (!consumer.isAnonymousNode(subject)) consumer.dataPropertyIRIs.add(subject);
    }
}
