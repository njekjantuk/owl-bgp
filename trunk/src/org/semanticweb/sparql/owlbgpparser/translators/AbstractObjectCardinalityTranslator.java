package org.semanticweb.sparql.owlbgpparser.translators;

import org.semanticweb.sparql.owlbgp.model.IRI;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.classexpressions.Clazz;
import org.semanticweb.sparql.owlbgp.model.literals.Literal;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;
import org.semanticweb.sparql.owlbgpparser.TripleConsumer;
import org.semanticweb.sparql.owlbgpparser.Vocabulary;

public abstract class AbstractObjectCardinalityTranslator extends AbstractObjectRestrictionTranslator {

    public AbstractObjectCardinalityTranslator(TripleConsumer consumer) {
        super(consumer);
    }
    protected abstract Identifier getCardinalityTriplePredicate();
    protected abstract Identifier getQualifiedCardinalityTriplePredicate();
    protected int translateCardinality(Identifier mainNode) {
        Literal con=(Literal)consumer.getLiteralObject(mainNode, getCardinalityTriplePredicate(), true);
        if (con==null) con=(Literal)consumer.getLiteralObject(mainNode, getQualifiedCardinalityTriplePredicate(), true);
        if (con == null) return -1;
        return Integer.parseInt(con.getLexicalForm().trim());
    }
    protected ClassExpression translateFiller(Identifier mainNode) {
        Identifier onClassObject=consumer.getResourceObject(mainNode, Vocabulary.OWL_ON_CLASS.getIRI(), true);
        if (onClassObject==null) return Clazz.THING;
        return consumer.translateClassExpression(onClassObject);
    }
    protected ClassExpression translateRestriction(Identifier mainNode) {
        ObjectPropertyExpression prop=translateOnProperty(mainNode);
        // TODO: check why we just return a class here
        if (prop == null) return Clazz.create((IRI)mainNode);
        int cardi=translateCardinality(mainNode);
        ClassExpression filler=translateFiller(mainNode);
        return createRestriction(prop, cardi, filler);
    }
    protected abstract ClassExpression createRestriction(ObjectPropertyExpression prop, int cardi, ClassExpression filler);
}
