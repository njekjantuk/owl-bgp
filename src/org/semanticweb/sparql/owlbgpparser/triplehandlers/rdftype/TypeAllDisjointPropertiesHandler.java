package org.semanticweb.sparql.owlbgpparser.triplehandlers.rdftype;

import java.util.HashSet;
import java.util.List;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.DisjointDataProperties;
import org.semanticweb.sparql.owlbgp.model.axioms.DisjointObjectProperties;
import org.semanticweb.sparql.owlbgp.model.properties.DataPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;
import org.semanticweb.sparql.owlbgpparser.TripleConsumer;
import org.semanticweb.sparql.owlbgpparser.Vocabulary;

public class TypeAllDisjointPropertiesHandler extends BuiltInTypeHandler {

    public TypeAllDisjointPropertiesHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_ALL_DISJOINT_PROPERTIES.getIRI());
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        return false;
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        consumer.consumeTriple(subject, predicate, object);
        Identifier listNode=consumer.getResourceObject(subject, Vocabulary.OWL_MEMBERS.getIRI(), true);
        Identifier first=consumer.getFirstResource(listNode, false);
        if (consumer.isObjectPropertyOnly(first)) {
            List<ObjectPropertyExpression> props=consumer.translateToObjectPropertyList(listNode);
            consumer.addAxiom(DisjointObjectProperties.create(new HashSet<ObjectPropertyExpression>(props),consumer.translateAnnotations(subject)));
        } else if (consumer.isDataPropertyOnly(first)) {
            List<DataPropertyExpression> props = consumer.translateToDataPropertyList(listNode);
            consumer.addAxiom(DisjointDataProperties.create(new HashSet<DataPropertyExpression>(props),consumer.translateAnnotations(subject)));
        } else 
             throw new RuntimeException("Could not disambiguate property type of property "+first+". A declaration is missing. ");
    }
}
