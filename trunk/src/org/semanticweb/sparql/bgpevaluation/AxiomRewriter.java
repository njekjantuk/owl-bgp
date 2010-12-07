package org.semanticweb.sparql.bgpevaluation;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.axioms.Axiom;
import org.semanticweb.sparql.owlbgp.model.axioms.ClassAssertion;
import org.semanticweb.sparql.owlbgp.model.axioms.DataPropertyAssertion;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.classexpressions.DataHasValue;
import org.semanticweb.sparql.owlbgp.model.classexpressions.DataSomeValuesFrom;
import org.semanticweb.sparql.owlbgp.model.dataranges.DataOneOf;
import org.semanticweb.sparql.owlbgp.model.dataranges.DataRange;

public class AxiomRewriter {
    public static void rewriteAxioms(Set<Axiom> queryAxioms) {
        rewriteImplicitDataPropertyAssertions(queryAxioms);
    }
    protected static void rewriteImplicitDataPropertyAssertions(Set<Axiom> queryAxioms) {
        for (Axiom axiom : queryAxioms) {
            if (axiom instanceof ClassAssertion) {
                ClassAssertion classAssertion=(ClassAssertion)axiom;
                ClassExpression ce=classAssertion.getClassExpression();
                if (ce instanceof DataHasValue) {
                    DataHasValue hasValue=(DataHasValue)ce;
                    DataPropertyAssertion dpa=DataPropertyAssertion.create(hasValue.getDataPropertyExpression(), classAssertion.getIndividual(), hasValue.getLiteral());
                    queryAxioms.remove(classAssertion);
                    queryAxioms.add(dpa);
                }
                if (ce instanceof DataSomeValuesFrom) {
                    DataSomeValuesFrom someValuesFrom=(DataSomeValuesFrom)ce;
                    DataRange dr=someValuesFrom.getDataRange();
                    if (dr instanceof DataOneOf) {
                        DataOneOf oneOf=(DataOneOf)dr;
                        if (oneOf.getLiterals().size()==1) { 
                            DataPropertyAssertion dpa=DataPropertyAssertion.create(someValuesFrom.getDataPropertyExpression(), classAssertion.getIndividual(), oneOf.getLiterals().iterator().next());
                            queryAxioms.remove(classAssertion);
                            queryAxioms.add(dpa);
                        }
                    }
                }
            }
        }
    }
}
