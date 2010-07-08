package org.semanticweb.sparql.owlbgpparser;

public class TypeObjectPropertyHandler extends BuiltInTypeHandler {

    public TypeObjectPropertyHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_OBJECT_PROPERTY.getIRI());
    }

    public void handleTriple(String subject, String predicate, String object) {
        if (consumer.isVariableNode(subject)) consumer.objectPropertyVars.add(subject);
        else if (!consumer.isAnonymousNode(subject)) consumer.objectPropertyIRIs.add(subject);
    }
}
