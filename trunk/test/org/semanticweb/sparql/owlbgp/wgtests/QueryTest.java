/*
   (c) Copyright 2005, 2006, 2007, 2008, 2009 Hewlett-Packard Development Company, LP
   All rights reserved.
   Adapted for OWL-BGP from the original source by Birte Glimm, 
   Oxford University Computing Laboraory, 2011.
   [See also end of file]

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

package  org.semanticweb.sparql.owlbgp.wgtests;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.openjena.atlas.logging.Log;
import org.openjena.riot.checker.CheckerLiterals;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.sparql.arq.OWLOntologyDataSet;

import com.hp.hpl.jena.query.ARQ;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryException;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFactory;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.shared.JenaException;
import com.hp.hpl.jena.sparql.algebra.Algebra;
import com.hp.hpl.jena.sparql.algebra.Op;
import com.hp.hpl.jena.sparql.engine.Plan;
import com.hp.hpl.jena.sparql.engine.QueryIterator;
import com.hp.hpl.jena.sparql.engine.ResultSetStream;
import com.hp.hpl.jena.sparql.engine.binding.Binding;
import com.hp.hpl.jena.sparql.engine.iterator.QueryIterPlainWrapper;
import com.hp.hpl.jena.sparql.junit.EarlReport;
import com.hp.hpl.jena.sparql.junit.EarlTestCase;
import com.hp.hpl.jena.sparql.junit.QueryTestException;
import com.hp.hpl.jena.sparql.junit.TestItem;
import com.hp.hpl.jena.sparql.resultset.ResultSetCompare;
import com.hp.hpl.jena.sparql.resultset.ResultSetRewindable;
import com.hp.hpl.jena.sparql.resultset.SPARQLResult;
import com.hp.hpl.jena.sparql.vocabulary.ResultSetGraphVocab;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.util.FileUtils;
import com.hp.hpl.jena.util.junit.TestUtils;
import com.hp.hpl.jena.vocabulary.RDF;

public class QueryTest extends EarlTestCase {
    protected static int testCounter=1;
    protected int testNumber=testCounter++;
    protected final TestItem testItem;
    protected final FileManager queryFileManager;
    protected SPARQLResult results;    // Maybe null if no testing of results
    protected boolean oldWarningFlag;
    
    // If supplied with a model, the test will load that model with data from the source
    // If no model is supplied one is created or attached (e.g. a database)
    public QueryTest(String testName, EarlReport earl, FileManager fm, TestItem t) {
        super(TestUtils.safeName(testName), t.getURI(), earl);
        queryFileManager=fm;
        testItem=t;
    }
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        // SPARQL and ARQ tests are done with no value matching (for query execution and results testing)
        ARQ.setTrue(ARQ.strictGraph);
        // Turn parser warnings off for the test data. 
        oldWarningFlag=CheckerLiterals.WarnOnBadLiterals;
        CheckerLiterals.WarnOnBadLiterals=false;
        // Sort out results.
        results= testItem.getResults();
    }
    @Override
    protected void tearDown() throws Exception {
        CheckerLiterals.WarnOnBadLiterals=oldWarningFlag;
        super.tearDown();
    }
    protected Dataset setUpDataset(Query query, TestItem testItem) {
        try {
            if (doesQueryHaveDataset(query) && doesTestItemHaveDataset(testItem)) {
                // Only warn if there are results to test
                // Syntax tests may have FROM etc and a manifest data file. 
                if (testItem.getResultFile()!=null)
                    Log.warn(this, testItem.getName()+" : query data source and also in test file"); 
            }
            // In test file?
            if (doesTestItemHaveDataset(testItem))
                // Not specified in the query - get from test item and load
                return createDataset(testItem.getDefaultGraphURIs(), testItem.getNamedGraphURIs());
          // Check 3 - were there any at all?
          if (!doesQueryHaveDataset(query)) 
              fail("No dataset");
          // Left to query
          return null;
        } catch (JenaException jEx) {
            fail("JenaException creating data source: "+jEx.getMessage());
            return null;
        }
    }
    protected static boolean doesTestItemHaveDataset(TestItem testItem) {
        boolean hasDefaultGraph=testItem.getDefaultGraphURIs()!=null&&testItem.getDefaultGraphURIs().size()>0;
        return hasDefaultGraph || (testItem.getNamedGraphURIs()!=null&&testItem.getNamedGraphURIs().size()>0);
    }
    protected static boolean doesQueryHaveDataset(Query query) {
        return query.hasDatasetDescription();
    }
    protected static Dataset createDataset(List<String> defaultGraphURIs, List<String> namedGraphURIs) {
        try {
            return new OWLOntologyDataSet(defaultGraphURIs, namedGraphURIs);
        } catch (OWLOntologyCreationException e) {
            fail("Could not parse the dataset into OWL ontologies.");
            return null;
        }
    }
    @Override
    protected void runTestForReal() throws Throwable {
        Query query=null;
        try {
            try { 
                query=queryFromTestItem(testItem);
//                writeOutAlgebra(query);
            } catch (QueryException qEx) {
                query=null;
                qEx.printStackTrace(System.err);
                fail("Parse failure: "+qEx.getMessage());
                throw qEx;
            }
            Dataset dataset=setUpDataset(query, testItem);
            if (dataset == null && !doesQueryHaveDataset(query)) 
                fail("No dataset for query");
            QueryExecution qe=null;
            if (dataset == null)
                qe=QueryExecutionFactory.create(query, queryFileManager);
            else
                qe=QueryExecutionFactory.create(query, dataset);
            try {
                if (query.isSelectType())
                    runTestSelect(query, qe);
                else if (query.isConstructType())
                    runTestConstruct(query, qe);
                else if (query.isDescribeType())
                    runTestDescribe(query, qe);
                else if (query.isAskType())
                    runTestAsk(query, qe);
            } finally { 
                qe.close(); 
            }
        } catch (IOException ioEx) {
            fail("IOException: "+ioEx.getMessage());
            throw ioEx;
        } catch (NullPointerException ex) { 
            throw ex; 
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
            fail("Exception: "+ex.getClass().getName()+": "+ex.getMessage());
        }
    }
    protected void writeOutAlgebra(Query query) {
        Op op=Algebra.compile(query);
        System.out.println("====================");
        System.out.println(op);
        System.out.println("--------------------");
        // Plan
        Plan plan=QueryExecutionFactory.createPlan(query, ModelFactory.createDefaultModel().getGraph());
        System.out.println(plan);
        System.out.println("====================");
    }
    protected void runTestSelect(Query query, QueryExecution qe) throws Exception {
        // Do the query!
        ResultSetRewindable resultsActual=ResultSetFactory.makeRewindable(qe.execSelect());
        // Turn into a resettable version
        //ResultSetRewindable results=ResultSetFactory.makeRewindable(resultsActual);
        qe.close();
        if (results==null)
            return;
        ResultSetRewindable resultsExpected=null;
        if (results.isResultSet())
            resultsExpected=ResultSetFactory.makeRewindable(results.getResultSet());
        else if (results.isModel())
            resultsExpected=ResultSetFactory.makeRewindable(results.getModel());
        else
            fail("Wrong result type for SELECT query") ;        
        if (query.isReduced()) {
            // Reduced - best we can do is DISTINCT
            resultsExpected=unique(resultsExpected) ;
            resultsActual=unique(resultsActual) ;
        }
        boolean equivalent=resultSetEquivalent(query, resultsExpected, resultsActual);
        if (!equivalent)
            printFailedResultSetTest(query, resultsExpected, resultsActual) ;
        assertTrue("Results do not match: "+testItem.getName(), equivalent) ;
        return ;
    }
    protected static ResultSetRewindable unique(ResultSetRewindable results) {
        // VERY crude.  Utilises the fact that bindings have value equality.
        List<Binding> x=new ArrayList<Binding>();
        Set<Binding> seen=new HashSet<Binding>();
        while (results.hasNext()) {
            Binding b=results.nextBinding();
            if (seen.contains(b))
                continue;
            seen.add(b);
            x.add(b);
        }
        QueryIterator qIter=new QueryIterPlainWrapper(x.iterator());
        ResultSet rs=new ResultSetStream(results.getResultVars(), ModelFactory.createDefaultModel(), qIter);
        return ResultSetFactory.makeRewindable(rs);
    } 
    public static boolean resultSetEquivalent(Query query, ResultSetRewindable resultsExpected, ResultSetRewindable resultsActual) {
        if (query.isOrdered())
            return ResultSetCompare.equalsByValueAndOrder(resultsExpected, resultsActual);
        else
            return ResultSetCompare.equalsByValue(resultsExpected, resultsActual);
    }
    protected void runTestConstruct(Query query, QueryExecution qe) throws Exception {
        // Do the query!
        Model resultsActual=qe.execConstruct();
        compareGraphResults(resultsActual, query);
    }
    protected void compareGraphResults(Model resultsActual, Query query) {
        if (results!=null) {
            try {
                if (!results.isGraph())
                    fail("Expected results are not a graph: "+testItem.getName());
                Model resultsExpected=results.getModel();
                if (!resultsExpected.isIsomorphicWith(resultsActual)) {
                    printFailedModelTest(query, resultsExpected, resultsActual);
                    fail("Results do not match: "+testItem.getName());
                }
            } catch (Exception ex) {
                String typeName=(query.isConstructType()?"construct":"describe");
                fail("Exception in result testing ("+typeName+"): "+ex);
            }
        }
    }
    protected void runTestDescribe(Query query, QueryExecution qe) throws Exception {
        Model resultsActual=qe.execDescribe();
        compareGraphResults(resultsActual, query);
    }
    protected void runTestAsk(Query query, QueryExecution qe) throws Exception {
        boolean result=qe.execAsk();
        if (results!=null) {
            if (results.isBoolean()) {
                boolean b=results.getBooleanResult();
                assertEquals("ASK test results do not match", b, result);
            } else {
                Model resultsAsModel=results.getModel();
                StmtIterator sIter=results.getModel().listStatements(null, RDF.type, ResultSetGraphVocab.ResultSet);
                if (!sIter.hasNext())
                    throw new QueryTestException("Can't find the ASK result");
                Statement s=sIter.nextStatement();
                if (sIter.hasNext())
                    throw new QueryTestException("Too many result sets in ASK result");
                Resource r=s.getSubject();
                Property p=resultsAsModel.createProperty(ResultSetGraphVocab.getURI()+"boolean");
                boolean x=r.getRequiredProperty(p).getBoolean();
                if (x!=result)
                    assertEquals("ASK test results do not match", x,result);
            }
        }        
        return;
    }
    protected void printFailedResultSetTest(Query query, ResultSetRewindable qrExpected, ResultSetRewindable qrActual) {
       PrintStream out=System.out;
       out.println();
       out.println("=======================================");
       out.println("Failure: "+makeDescription());
       out.println("Query: \n"+query);
       out.println("Got: "+qrActual.size()+" --------------------------------");
       qrActual.reset();
       ResultSetFormatter.out(out, qrActual, query.getPrefixMapping());
       qrActual.reset();
       out.flush();
       out.println("Expected: "+qrExpected.size()+" -----------------------------");
       qrExpected.reset();
       ResultSetFormatter.out(out, qrExpected, query.getPrefixMapping());
       qrExpected.reset();       
       out.println();
       out.flush();
    }
    protected void printFailedModelTest(Query query, Model expected, Model results) {
        PrintWriter out=FileUtils.asPrintWriterUTF8(System.out);
        out.println("=======================================");
        out.println("Failure: "+makeDescription());
        results.write(out, "TTL");
        out.println("---------------------------------------");
        expected.write(out, "TTL");
        out.println();
    }
    @Override
    public String toString() { 
        if (testItem.getName()!=null)
            return testItem.getName();
        return super.getName();
    }
    protected String makeDescription() {
        String tmp="";
        if (testItem.getDefaultGraphURIs()!=null)
            for (Iterator<String> iter=testItem.getDefaultGraphURIs().iterator(); iter.hasNext();)
                tmp=tmp+iter.next();
        if (testItem.getNamedGraphURIs()!=null)
            for (Iterator<String> iter=testItem.getNamedGraphURIs().iterator(); iter.hasNext();)
                tmp=tmp+iter.next();
        return "Test "+testNumber+" :: "+testItem.getName();
    }
}

/*
 * (c) Copyright 2005, 2006, 2007, 2008, 2009 Hewlett-Packard Development Company, LP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
