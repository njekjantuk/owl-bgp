package org.semanticweb.sparql.owlbgp.parser.triplehandlers;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Annotation;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.UntypedVariable;
import org.semanticweb.sparql.owlbgp.model.axioms.DataPropertyAssertion;
import org.semanticweb.sparql.owlbgp.model.axioms.ObjectPropertyAssertion;
import org.semanticweb.sparql.owlbgp.model.individuals.Individual;
import org.semanticweb.sparql.owlbgp.model.literals.Literal;
import org.semanticweb.sparql.owlbgp.model.literals.LiteralVariable;
import org.semanticweb.sparql.owlbgp.model.properties.DataPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;

public class PropertyAssertionHandler extends TripleHandler {

    public PropertyAssertionHandler(TripleConsumer consumer) {
        super(consumer);
    }
    
    @Override
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object, Set<Annotation> annotations) {
        Individual individual=consumer.getIND(subject);
        ObjectPropertyExpression ope=consumer.getOPE(predicate);
        if (ope!=null) {
            Individual individual2=consumer.getIND(object);
            if (individual2!=null)
                consumer.addAxiom(ObjectPropertyAssertion.create(ope,individual,individual2,annotations));
            else 
                throw new RuntimeException("Could not find an individual for the object in the triple "+subject+" "+predicate+" "+object+", but "+predicate+" is an object property. ");
        } else {
            DataPropertyExpression dpe=consumer.getDPE(predicate);
            Literal literal=null;
            if (object instanceof UntypedVariable)
                literal=LiteralVariable.create((UntypedVariable)object);
            else if (object instanceof Literal) 
                literal=(Literal)object;
            if (literal==null)
                throw new RuntimeException("It seems "+object+" is not a literal although the triple "+subject+" "+predicate+" "+object+" seems to be a data property assertion since "+predicate+" is a data property. ");
            consumer.addAxiom(DataPropertyAssertion.create(dpe,individual,literal,annotations));
        }
    }
}
