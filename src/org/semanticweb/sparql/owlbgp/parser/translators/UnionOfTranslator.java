package org.semanticweb.sparql.owlbgp.parser.translators;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ObjectUnionOf;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;

public class UnionOfTranslator extends AbstractNaryBooleanClassExpressionTranslator {

    public UnionOfTranslator(TripleConsumer consumer) {
        super(consumer);
    }
    protected ClassExpression createClassExpression(Set<ClassExpression> operands) {
        return ObjectUnionOf.create(operands);
    }
    protected Identifier getPredicateIRI() {
        return Vocabulary.OWL_UNION_OF.getIRI();
    }
}
