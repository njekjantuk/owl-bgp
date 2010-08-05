package org.semanticweb.sparql.owlbgp.parser;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AllParserTests extends TestCase {

    public static Test suite() {
        TestSuite suite = new TestSuite("Unit tests for the parser.");
        // $JUnit-BEGIN$
        suite.addTestSuite(TestAnnotationParsing.class);
        suite.addTestSuite(TestClassExpressionParsing.class);
        suite.addTestSuite(TestDataRangeParsing.class);
        suite.addTestSuite(TestDeclarationFixing.class);
        suite.addTestSuite(TestPropertyParsing.class);
        // $JUnit-END$
        return suite;
    }
}
