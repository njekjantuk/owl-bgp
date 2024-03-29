/* Copyright 2010-2012 by the developers of the OWL-BGP project. 

   This file is part of OWL-BGP.

   OWL-BGP is free software: you can redistribute it and/or modify
   it under the terms of the GNU Lesser General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   OWL-BGP is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU Lesser General Public License for more details.

   You should have received a copy of the GNU Lesser General Public License
   along with OWL-BGP. If not, see <http://www.gnu.org/licenses/>.
 */

package  org.semanticweb.sparql.owlbgp;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.semanticweb.sparql.owlbgp.evaluation.AllEvaluationTests;
import org.semanticweb.sparql.owlbgp.model.AllModelTests;
import org.semanticweb.sparql.owlbgp.parser.AllParserTests;

public class AllTests extends TestCase {

    public static Test suite() {
        TestSuite suite = new TestSuite("All unit tests for the OWL BGP project.");
        // $JUnit-BEGIN$
        suite.addTest(AllParserTests.suite());
        suite.addTest(AllModelTests.suite());
        suite.addTest(AllEvaluationTests.suite());
        // $JUnit-END$
        return suite;
    }
}
