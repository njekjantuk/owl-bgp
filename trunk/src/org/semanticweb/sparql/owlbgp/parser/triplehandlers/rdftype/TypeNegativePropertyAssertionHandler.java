package org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.NegativeDataPropertyAssertion;
import org.semanticweb.sparql.owlbgp.model.axioms.NegativeObjectPropertyAssertion;
import org.semanticweb.sparql.owlbgp.model.individuals.Individual;
import org.semanticweb.sparql.owlbgp.model.literals.Literal;
import org.semanticweb.sparql.owlbgp.model.properties.DataPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;

public class TypeNegativePropertyAssertionHandler extends BuiltInTypeHandler {

    public TypeNegativePropertyAssertionHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_NEGATIVE_PROPERTY_ASSERTION.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        return false;
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        Identifier source=consumer.getResourceObject(subject, Vocabulary.OWL_SOURCE_INDIVIDUAL.getIRI(), true);
        Individual sourceInd=consumer.translateIndividual(source);
        Identifier property=consumer.getResourceObject(subject, Vocabulary.OWL_ASSERTION_PROPERTY.getIRI(), true);
        Identifier target=consumer.getResourceObject(subject, Vocabulary.OWL_TARGET_INDIVIDUAL.getIRI(), true);
        if (target!=null) { 
            ObjectPropertyExpression prop=consumer.translateObjectPropertyExpression(property);
            Individual targetInd=consumer.translateIndividual(target);
            consumer.addAxiom(NegativeObjectPropertyAssertion.create(prop,sourceInd,targetInd,consumer.getAnnotations(subject)));
        } else {
            DataPropertyExpression prop=consumer.translateDataPropertyExpression(property);
            Literal targetLit=consumer.getLiteralObject(subject, Vocabulary.OWL_TARGET_VALUE.getIRI(), true);
            consumer.addAxiom(NegativeDataPropertyAssertion.create(prop,sourceInd,targetLit,consumer.getAnnotations(subject)));
        }
        consumer.consumeTriple(subject, predicate, object);
    }
}
