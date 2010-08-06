package org.semanticweb.sparql.owlbgp;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.semanticweb.sparql.owlbgp.model.AllModelTests;
import org.semanticweb.sparql.owlbgp.parser.AllParserTests;

public class AllTests extends TestCase {

    public static Test suite() {
        TestSuite suite = new TestSuite("All unit tests for the OWL BGP project.");
        // $JUnit-BEGIN$
        suite.addTest(AllParserTests.suite());
        suite.addTest(AllModelTests.suite());
        // $JUnit-END$
        return suite;
    }
}
