package org.semanticweb.sparql.owlbgpparser;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.ObjectUnionOf;

public class UnionOfTranslator extends AbstractNaryBooleanClassExpressionTranslator {

    public UnionOfTranslator(OWLRDFConsumer consumer) {
        super(consumer);
    }
    protected ClassExpression createClassExpression(Set<ClassExpression> operands) {
        return ObjectUnionOf.create(operands);
    }
    protected Identifier getPredicateIRI() {
        return Vocabulary.OWL_UNION_OF.getIRI();
    }
}
