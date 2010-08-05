package org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import java.util.List;
import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Annotation;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.SubObjectPropertyOf;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyChain;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.TriplePredicateHandler;

public class TPPropertyChainAxiomHandler extends TriplePredicateHandler {

    public TPPropertyChainAxiomHandler(TripleConsumer consumer) {
        super(consumer, Vocabulary.OWL_PROPERTY_CHAIN_AXIOM);
    }

    @Override
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object, Set<Annotation> annotations) {
        ObjectPropertyExpression superProperty=consumer.getObjectPropertyExpressionForObjectPropertyIdentifier(subject);
        if (superProperty==null)
            throw new RuntimeException("Could not find an object property expression for the subject in the triple "+subject+" "+Vocabulary.OWL_PROPERTY_CHAIN_AXIOM+" "+object+". ");
        else {
            List<ObjectPropertyExpression> chainList=consumer.translateToObjectPropertyExpressionList(object);
            if (chainList!=null) {
                if (chainList.size()>1)
                    consumer.addAxiom(SubObjectPropertyOf.create(ObjectPropertyChain.create(chainList),superProperty,annotations));
                else 
                    consumer.addAxiom(SubObjectPropertyOf.create(chainList.iterator().next(),superProperty,annotations));
            } else {
                throw new RuntimeException("Error: A list representing the properties of a property chain could not be assembled. The main triple is: "+subject+" "+Vocabulary.OWL_PROPERTY_CHAIN_AXIOM.toString()+" "+object+" and "+object+" is the main node for the list. ");
            }
        }
    }

}
