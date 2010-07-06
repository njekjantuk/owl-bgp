package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.ClassExpression;

/**
 * Give a node in an RDF graph, which represents the main node
 * of a class expression, the <code>ClassExpressionTranslator</code>
 * consumes the triples that represent the class expression, and
 * translates the triples to the appropriate <code>ClassExpression</code>
 * object.
 */
public interface ClassExpressionTranslator {
    ClassExpression translate(String mainNode);
}
