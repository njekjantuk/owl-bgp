package org.semanticweb.sparql.owlbgpparser;

public class TypeLiteralVariableHandler extends BuiltInTypeHandler {

    public TypeLiteralVariableHandler(OWLRDFConsumer consumer) {
        super(consumer, OWLRDFVocabulary.OWL_LITERAL.getIRI());
    }
    
    public void handleTriple(String subject, String predicate, String object) {
        if (consumer.isVariableNode(subject)) consumer.literalVars.add(subject);
    }
}