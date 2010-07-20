package org.semanticweb.sparql.owlbgpparser.translators;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ObjectIntersectionOf;
import org.semanticweb.sparql.owlbgpparser.TripleConsumer;
import org.semanticweb.sparql.owlbgpparser.Vocabulary;

public class IntersectionOfTranslator extends AbstractNaryBooleanClassExpressionTranslator {
    public IntersectionOfTranslator(TripleConsumer consumer) {
        super(consumer);
    }
    protected ClassExpression createClassExpression(Set<ClassExpression> operands) {
        return ObjectIntersectionOf.create(operands);
    }
    protected Identifier getPredicateIRI() {
        return Vocabulary.OWL_INTERSECTION_OF.getIRI();
    }
}
