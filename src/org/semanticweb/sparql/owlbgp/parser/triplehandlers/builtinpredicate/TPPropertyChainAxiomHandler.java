package org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Annotation;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.SubObjectPropertyOf;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyChain;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.properties.PropertyExpression;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.AbstractResourceTripleHandler;

public class TPPropertyChainAxiomHandler extends AbstractResourceTripleHandler {

    public TPPropertyChainAxiomHandler(TripleConsumer consumer) {
        super(consumer);
    }

    @Override
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object, Set<Annotation> annotations) {
        ObjectPropertyExpression superProperty=consumer.getObjectPropertyExpressionForObjectPropertyIdentifier(subject);
        if (superProperty==null)
            throw new RuntimeException("Could not find an object property expression for the subject in the triple "+subject+" "+Vocabulary.OWL_PROPERTY_CHAIN_AXIOM+" "+object+". ");
        else {
            List<PropertyExpression> chainList=consumer.translateToPropertyExpressionList(object);
            if (chainList!=null) {
                List<ObjectPropertyExpression> opes=new ArrayList<ObjectPropertyExpression>();
                for (PropertyExpression pe : chainList)
                    if (pe instanceof ObjectPropertyExpression)
                        opes.add((ObjectPropertyExpression)pe);
                    else 
                        throw new RuntimeException("Error: A list representing the object properties of a property chain contains a property that is not an object property expression. The main triple is: "+subject+" "+Vocabulary.OWL_PROPERTY_CHAIN_AXIOM.toString()+" "+object+" and "+object+" is the main node for the list. ");
                if (opes.size()>1) {
                    consumer.addAxiom(SubObjectPropertyOf.create(ObjectPropertyChain.create(opes),superProperty,annotations));
                } else 
                    consumer.addAxiom(SubObjectPropertyOf.create(opes.iterator().next(),superProperty,annotations));
            } else {
                throw new RuntimeException("Error: A list representing the properties of a property chain could not be assembled. The main triple is: "+subject+" "+Vocabulary.OWL_PROPERTY_CHAIN_AXIOM.toString()+" "+object+" and "+object+" is the main node for the list. ");
            }
        }
    }

}
