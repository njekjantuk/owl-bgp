package org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.dataranges.DataRange;
import org.semanticweb.sparql.owlbgp.model.dataranges.Datatype;
import org.semanticweb.sparql.owlbgp.model.literals.TypedLiteral;
import org.semanticweb.sparql.owlbgp.model.properties.PropertyExpression;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.AbstractResourceTripleHandler;

public abstract class AbstractCardinalityHandler extends AbstractResourceTripleHandler {

    protected final Identifier predicateIRI;
    
    public AbstractCardinalityHandler(TripleConsumer consumer, Identifier predicateIRI) {
        super(consumer);
        this.predicateIRI=predicateIRI;
    }
    protected PropertyExpression getPropertyExpression(Identifier subject) {
        Identifier propID=consumer.getObject(subject, Vocabulary.OWL_ON_PROPERTY, true);
        PropertyExpression pe=consumer.getObjectPropertyExpressionForObjectPropertyIdentifier(propID);
        if (pe!=null) 
            return pe;
        else {
            pe=consumer.getDataPropertyExpressionForDataPropertyIdentifier(propID);
            if (pe!=null)
                return pe;
        }
        // TODO: error handling
        throw new RuntimeException("error");
    }
    protected ClassExpression getClassExpression(Identifier subject) {
        Identifier classID=consumer.getObject(subject, Vocabulary.OWL_ON_CLASS, true);
        if (classID==null) return null;
        consumer.translateClassExpression(classID);
        return consumer.getClassExpressionForClassIdentifier(classID);
    }
    protected DataRange getDataRange(Identifier subject) {
        Identifier dataRangeID=consumer.getObject(subject, Vocabulary.OWL_ON_DATA_RANGE, true);
        if (dataRangeID==null) return null;
        consumer.translateDataRange(dataRangeID);
        return consumer.getDataRangeForDataRangeIdentifier(dataRangeID);
    }
    protected int getCardinality(Identifier object) {
        if (!(object instanceof TypedLiteral)) {
            throw new IllegalArgumentException("Error: The term "+object+" is supposed to be a non-negative integer, i.e., a literal with datatype xsd:nonNegativeInteger, because it represents the number in a number restriction and occurs in a triple with predicate "+predicateIRI+". ");
        } else {
            TypedLiteral lit=(TypedLiteral)object;
            if (lit.getDatatype()!=Datatype.XSD_NON_NEGATIVE_INTEGER) {
                // TODO: error handling
                throw new IllegalArgumentException("Error: The term "+object+" is supposed to be a non-negative integer, i.e., a literal with datatype xsd:nonNegativeInteger, because it represents the number in a number restriction and occurs in a triple with predicate "+predicateIRI+". Here the datatype is: "+lit.getDatatype());
            } else {
                try {
                    int n=Integer.parseInt(lit.getLexicalForm());
                    if (n<0)
                        throw new IllegalArgumentException("Error: The literal object "+lit+" in a triple with predicate "+predicateIRI+" denotes an integer smaller than zero, which is not allowed for numbers in a number restriction. ");
                    else 
                        return n;
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Error: The literal object "+lit+" in a triple with predicate "+predicateIRI+" cannot be parsed into an integer, which is required since it denotes a number in a number restriction. ");
                }
            }
        }
    }
}
