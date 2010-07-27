package org.semanticweb.sparql.owlbgp.parser.translators;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ObjectIntersectionOf;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;

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
