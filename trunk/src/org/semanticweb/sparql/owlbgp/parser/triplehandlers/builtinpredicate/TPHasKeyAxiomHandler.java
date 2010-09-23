package org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Annotation;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.HasKey;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.properties.DataPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.properties.PropertyExpression;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.TripleHandler;

public class TPHasKeyAxiomHandler extends TripleHandler {

    public TPHasKeyAxiomHandler(TripleConsumer consumer) {
        super(consumer);
    }

    @Override
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object, Set<Annotation> annotations) {
        //T(CE) owl:hasKey T(SEQ OPE1 ... OPEm DPE1 ... DPEn ) .
        ClassExpression cls=consumer.getClassExpressionForClassIdentifier(subject);
        if (cls==null)
            throw new RuntimeException("Could not find  class expression for the subject in the triple "+subject+" "+Vocabulary.OWL_HAS_KEY+" "+object+". ");
        else {
            List<PropertyExpression> properties=consumer.translateToPropertyExpressionList(object);
            if (properties==null || properties.size()==0)
                throw new RuntimeException("Error: A list representing the properties of a HasKey axiom could not be assembled. The main triple is: "+subject+" "+Vocabulary.OWL_HAS_KEY.toString()+" "+object+" and "+object+" is the main node for the list. ");
            else {
                for (PropertyExpression pe : properties)
                    if (!(pe instanceof ObjectPropertyExpression) && !(pe instanceof DataPropertyExpression))
                        throw new RuntimeException("Error: A list representing object and data properties of a HasKey axiom contain a property that is neither an object nor a data property expression. The main triple is: "+subject+" "+Vocabulary.OWL_HAS_KEY.toString()+" "+object+" and "+object+" is the main node for the list. ");
                consumer.addAxiom(HasKey.create(cls, new HashSet<PropertyExpression>(properties), annotations));
            }
        }
    }

}
