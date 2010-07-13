package org.semanticweb.sparql.owlbgpparser;

public class TypeLiteralVariableHandler extends BuiltInTypeHandler {

    public TypeLiteralVariableHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_LITERAL.getIRI());
    }
    
    public void handleTriple(String subject, String predicate, String object) {
        if (consumer.isVariableNode(subject)) consumer.literalVariables.add(subject);
    }
}