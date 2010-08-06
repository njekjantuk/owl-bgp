package org.semanticweb.sparql.owlbgp.model;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AllModelTests extends TestCase {

    public static Test suite() {
        TestSuite suite = new TestSuite("Unit tests for the model classes.");
        // $JUnit-BEGIN$
        suite.addTestSuite(TestClasses.class);
        // $JUnit-END$
        return suite;
    }
}
