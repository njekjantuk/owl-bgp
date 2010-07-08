package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.Clazz;
import org.semanticweb.sparql.owlbgp.model.ILiteral;
import org.semanticweb.sparql.owlbgp.model.ObjectPropertyExpression;

public abstract class AbstractObjectCardinalityTranslator extends AbstractObjectRestrictionTranslator {

    public AbstractObjectCardinalityTranslator(OWLRDFConsumer consumer) {
        super(consumer);
    }
    protected abstract String getCardinalityTriplePredicate();
    protected abstract String getQualifiedCardinalityTriplePredicate();
    protected int translateCardinality(String mainNode) {
        ILiteral con=(ILiteral)getLiteralObject(mainNode, getCardinalityTriplePredicate(), true);
        if (con==null) con=(ILiteral)getLiteralObject(mainNode, getQualifiedCardinalityTriplePredicate(), true);
        if (con == null) return -1;
        return Integer.parseInt(con.getLexicalForm().trim());
    }
    protected ClassExpression translateFiller(String mainNode) {
        String onClassObject=getResourceObject(mainNode, Vocabulary.OWL_ON_CLASS.getIRI(), true);
        if (onClassObject==null) return Clazz.THING;
        return translateToClassExpression(onClassObject);
    }
    protected ClassExpression translateRestriction(String mainNode) {
        ObjectPropertyExpression prop=translateOnProperty(mainNode);
        if (prop == null) return Clazz.create(mainNode);
        int cardi=translateCardinality(mainNode);
        ClassExpression filler=translateFiller(mainNode);
        return createRestriction(prop, cardi, filler);
    }
    protected abstract ClassExpression createRestriction(ObjectPropertyExpression prop, int cardi, ClassExpression filler);
}
