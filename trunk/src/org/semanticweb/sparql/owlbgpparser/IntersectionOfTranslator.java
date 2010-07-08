package org.semanticweb.sparql.owlbgpparser;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.ObjectIntersectionOf;

public class IntersectionOfTranslator extends AbstractNaryBooleanClassExpressionTranslator {
    public IntersectionOfTranslator(OWLRDFConsumer consumer) {
        super(consumer);
    }
    protected ClassExpression createClassExpression(Set<ClassExpression> operands) {
        return ObjectIntersectionOf.create(operands);
    }
    protected String getPredicateIRI() {
        return Vocabulary.OWL_INTERSECTION_OF.getIRI();
    }
}
