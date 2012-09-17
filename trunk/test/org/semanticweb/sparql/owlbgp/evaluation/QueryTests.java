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

package  org.semanticweb.sparql.owlbgp.evaluation;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.coode.owlapi.turtle.TurtleOntologyFormat;
import org.semanticweb.sparql.OWLReasonerSPARQLEngine;
import org.semanticweb.sparql.bgpevaluation.monitor.PrintingMonitor;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;

public class QueryTests extends AbstractQueryTest {
    public static final String LB = System.getProperty("line.separator") ;
    
    public QueryTests() {
        this(null);
    }
    public QueryTests(String name) {
        super(name);
    }
    
    protected String getAxioms() {
        String axioms="Declaration( Class( :GraduateAssistant ) )"+
                "Declaration( Class( :Student ) )"+
                "Declaration( Class( :Employee ) )"+
                "Declaration( Class( :ConferencePaper ) )"+
                "Declaration( Class( :Conference ) )"+
                "Declaration( Class( :Workshop ) )"+
                "Declaration( ObjectProperty( :publishedAt ) )"+LB+
                "Declaration( ObjectProperty( :hasPublication ) )"+LB+
                "Declaration( DataProperty( :name ) )"+LB+
                "Declaration( NamedIndividual( :John ) )"+LB+
                "Declaration( NamedIndividual( :Anite ) )"+LB+
                "Declaration( NamedIndividual( :George ) )"+LB+
                "Declaration( NamedIndividual( :paper1 ) )"+LB+
                "Declaration( NamedIndividual( :person1 ) )"+LB+
                "ClassAssertion( :GraduateAssistant :John )"+LB+
                "ClassAssertion( :ConferencePaper :paper1 )"+LB+
                "SubClassOf( :GraduateAssistant :Student )"+LB+
                "SubClassOf( :GraduateAssistant :Employee )"+LB+
                "ClassAssertion( :Student :Anite )"+LB+
                "ClassAssertion( :Employee :George )"+LB+
                "ObjectPropertyAssertion( :hasPublication :John :paper1 )"+LB+
                "ObjectPropertyAssertion( :hasPublication :person1 :paper1 )"+LB+
                "DataPropertyAssertion( :name :John \"Johnnie\" )"+LB+
                "SubClassOf( :ConferencePaper ObjectSomeValuesFrom(:publishedAt :Conference) )"+LB+
                "DisjointClasses( :Conference :Workshop )";
        return axioms;
    }
            
    public void testOPParsing() throws Exception {
        loadDataSetWithAxioms(getAxioms());
        String s= "PREFIX ex:   <http://example.org/> "+LB
                + "PREFIX :      <http://example.org/>" +LB
                + "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +LB
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "+LB
                + "PREFIX owl:  <http://www.w3.org/2002/07/owl#> "+LB
                + "SELECT ?x WHERE {"+LB
                + "  ?x ex:hasPublication _:b0 . "+LB
                + "  _:b0 rdf:type [ "+LB
                + "    owl:onProperty ex:publishedAt ; "+LB
                + "    rdf:type owl:Restriction ; "+LB
                + "    owl:someValuesFrom [ "+LB
                + "      rdf:type owl:Class ; "+LB
                + "      owl:complementOf ex:Workshop ] "+LB
                + "   ] "+LB
                + "}";
        
        OWLReasonerSPARQLEngine sparqlEngine=new OWLReasonerSPARQLEngine(new PrintingMonitor());
        Query query=QueryFactory.create(s);
        ResultSet result=sparqlEngine.execQuery(query,m_dataSet);
        int noResults=0;
        while (result.hasNext()) {
            result.next();
            noResults++;
        }
        assertTrue(noResults==2);
    }
    public void testInstanceOfComplexClass() throws Exception {
        loadDataSetWithAxioms(getAxioms());
        String s= "PREFIX ex:   <http://example.org/> "+LB
                + "PREFIX :      <http://example.org/>" +LB
                + "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +LB
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "+LB
                + "PREFIX owl:  <http://www.w3.org/2002/07/owl#> "+LB
                + "SELECT ?x ?y WHERE {"+LB
                + "  ?x rdf:type _:b0 . "+LB
                //+ "  _:b0 rdf:type owl:Class . "+LB
                + "  _:b0 owl:intersectionOf _:b1 . "+LB
                + "  _:b1 rdf:first ex:Student . "+LB
                + "  _:b1 rdf:rest _:b2 . "+LB
                + "  _:b2 rdf:first ex:Employee . "+LB
                + "  _:b2 rdf:rest rdf:nil . "+LB
                + "  ?x ex:name ?y "+LB
                + "}";
        
        OWLReasonerSPARQLEngine sparqlEngine=new OWLReasonerSPARQLEngine();
        Query query=QueryFactory.create(s);
        ResultSet result=sparqlEngine.execQuery(query,m_dataSet);
        int noResults=0;
        while (result.hasNext()) {
            result.next();
            noResults++;
        }
        assertTrue(noResults==1);
    }
    public void testSubClassQuery() throws Exception {
        loadDataSetWithAxioms(getAxioms());
        String s= "PREFIX  ex:   <http://example.org/> "+LB
                + "PREFIX :      <http://example.org/>" +LB
                + "PREFIX  rdfs: <http://www.w3.org/2000/01/rdf-schema#> "+LB
                + "SELECT ?c WHERE {"+LB
                + "  ?c rdfs:subClassOf ex:Student "+LB
                + "}";
        
        OWLReasonerSPARQLEngine sparqlEngine=new OWLReasonerSPARQLEngine();
        Query query=QueryFactory.create(s);
        ResultSet result=sparqlEngine.execQuery(query,m_dataSet);
        int noResults=0;
        while (result.hasNext()) {
            result.next();
            noResults++;
        }
        assertTrue(noResults==3);
    }
    
    public void testOPSuccessors() throws Exception {
        loadDataSetWithAxioms(getAxioms());
        
        File newOntologyFile=new File("/Users/bglimm/Documents/paper-sparqldl-data.ttl");
        newOntologyFile=newOntologyFile.getAbsoluteFile();
        // Now we create a buffered stream since the ontology manager can then write to that stream. 
        BufferedOutputStream outputStream=new BufferedOutputStream(new FileOutputStream(newOntologyFile));
        // We use the same format as for the input ontology.
        m_ontologyManager.saveOntology(m_ontology, new TurtleOntologyFormat(), outputStream);
        
        String s= "PREFIX ex:   <http://example.org/> "+LB
                + "PREFIX :     <http://example.org/>" +LB
                + "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +LB
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "+LB
                + "PREFIX owl:  <http://www.w3.org/2002/07/owl#> "+LB
                + "SELECT ?p ?v WHERE { "+LB
                + "  ?p rdf:type owl:ObjectProperty . ex:John ?p ?v "+LB
                + "}";
        
        OWLReasonerSPARQLEngine sparqlEngine=new OWLReasonerSPARQLEngine();
        Query query=QueryFactory.create(s);
        ResultSet result=sparqlEngine.execQuery(query,m_dataSet);
        int noResults=0;
        while (result.hasNext()) {
            result.next();
            noResults++;
        }
        assertTrue(noResults==1);
    }
}