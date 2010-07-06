package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.DataPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.ILiteral;
import org.semanticweb.sparql.owlbgp.model.Individual;
import org.semanticweb.sparql.owlbgp.model.NegativeDataPropertyAssertion;
import org.semanticweb.sparql.owlbgp.model.NegativeObjectPropertyAssertion;
import org.semanticweb.sparql.owlbgp.model.ObjectPropertyExpression;

public class TypeNegativePropertyAssertionHandler extends BuiltInTypeHandler {

    public TypeNegativePropertyAssertionHandler(OWLRDFConsumer consumer) {
        super(consumer, OWLRDFVocabulary.OWL_NEGATIVE_PROPERTY_ASSERTION.getIRI());
    }

    public boolean canHandleStreaming(String subject, String predicate, String object) {
        return false;
    }
    public void handleTriple(String subject, String predicate, String object) {
        String source=consumer.getResourceObject(subject, OWLRDFVocabulary.OWL_SOURCE_INDIVIDUAL.getIRI(), true);
        if (source==null) 
            source=consumer.getResourceObject(subject, OWLRDFVocabulary.OWL_SUBJECT.getIRI(), true);
        if (source==null)
            source=consumer.getResourceObject(subject, OWLRDFVocabulary.RDF_SUBJECT.getIRI(), true);

        String property=consumer.getResourceObject(subject, OWLRDFVocabulary.OWL_ASSERTION_PROPERTY.getIRI(), true);
        if (property==null)
            property=consumer.getResourceObject(subject, OWLRDFVocabulary.OWL_PREDICATE.getIRI(), true);
        if (property==null)
            property=consumer.getResourceObject(subject, OWLRDFVocabulary.RDF_PREDICATE.getIRI(), true);

        Object target=consumer.getResourceObject(subject, OWLRDFVocabulary.OWL_TARGET_INDIVIDUAL.getIRI(), true);
        if (target==null)
            target=consumer.getResourceObject(subject, OWLRDFVocabulary.OWL_OBJECT.getIRI(), true);
        if (target==null)
            target=consumer.getResourceObject(subject, OWLRDFVocabulary.RDF_OBJECT.getIRI(), true);
        if (target==null)
            target=consumer.getLiteralObject(subject, OWLRDFVocabulary.OWL_OBJECT.getIRI(), true);
        if (target==null)
            target=consumer.getLiteralObject(subject, OWLRDFVocabulary.RDF_OBJECT.getIRI(), true);
        if (target==null)
            target=consumer.getLiteralObject(subject, OWLRDFVocabulary.OWL_TARGET_VALUE.getIRI(), true);

        consumer.translateAnnotations(subject);
        if (target instanceof ILiteral) {
            Individual sourceInd=consumer.translateIndividual(source);
            DataPropertyExpression prop=consumer.translateDataPropertyExpression(property);
            consumeTriple(subject, predicate, object);
            addAxiom(NegativeDataPropertyAssertion.create(prop,sourceInd,(ILiteral)target));
        } else {
            Individual sourceInd=consumer.translateIndividual(source);
            ObjectPropertyExpression prop=consumer.translateObjectPropertyExpression(property);
            Individual targetInd=consumer.translateIndividual(target.toString());
            consumeTriple(subject, predicate, object);
            addAxiom(NegativeObjectPropertyAssertion.create(prop,sourceInd,targetInd));
        }

    }
}
