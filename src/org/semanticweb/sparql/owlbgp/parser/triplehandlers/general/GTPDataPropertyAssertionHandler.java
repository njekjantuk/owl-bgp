package org.semanticweb.sparql.owlbgp.parser.triplehandlers.general;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.DataPropertyAssertion;
import org.semanticweb.sparql.owlbgp.model.dataranges.Facet;
import org.semanticweb.sparql.owlbgp.model.literals.Literal;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.AbstractLiteralTripleHandler;

public class GTPDataPropertyAssertionHandler extends AbstractLiteralTripleHandler {

    public GTPDataPropertyAssertionHandler(TripleConsumer consumer) {
        super(consumer);
    }
    public boolean canHandle(Identifier subject, Identifier predicate, Literal object) {
        if (consumer.isAnnotationProperty(predicate) || consumer.isOntology(subject)) return false;
        return !Vocabulary.BUILT_IN_VOCABULARY_IRIS.contains(predicate) && !Facet.OWL_FACETS.contains(predicate);
    }
    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Literal object) {
        return false;
    }
    public void handleTriple(Identifier subject, Identifier predicate, Literal object) {
        consumer.addAxiom(DataPropertyAssertion.create(consumer.translateDataPropertyExpression(predicate),consumer.translateIndividual(subject), object));
        consumer.consumeTriple(subject, predicate, object);
    }
}
