package org.semanticweb.sparql.owlbgp.wgtests;

import junit.framework.Test;

import com.hp.hpl.jena.query.Syntax;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.impl.PropertyImpl;
import com.hp.hpl.jena.rdf.model.impl.ResourceImpl;
import com.hp.hpl.jena.sparql.core.DataFormat;
import com.hp.hpl.jena.sparql.junit.OWLBGPQueryTest;
import com.hp.hpl.jena.sparql.junit.QueryTestException;
import com.hp.hpl.jena.sparql.junit.ScriptTestSuiteFactory;
import com.hp.hpl.jena.sparql.junit.TestItem;
import com.hp.hpl.jena.sparql.junit.TestQueryUtils;
import com.hp.hpl.jena.sparql.vocabulary.TestManifest;
import com.hp.hpl.jena.sparql.vocabulary.TestManifestUpdate_11;
import com.hp.hpl.jena.sparql.vocabulary.TestManifestX;
import com.hp.hpl.jena.sparql.vocabulary.TestManifest_11;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.util.junit.TestUtils;
import com.hp.hpl.jena.vocabulary.RDF;

public class OWLBGPScriptTestSuitFactory extends ScriptTestSuiteFactory {
    
    public static final String SD="http://www.w3.org/ns/sparql-service-description#";
    public static final String ENT="http://www.w3.org/ns/entailment/";
    
    private FileManager fileManager=FileManager.get();
    
    @Override
    public Test makeTest(Resource manifest,Resource entry,String testName,Resource action,Resource result) {
//        if (testName.startsWith("sparqldl-10")) {
            if (action==null) {
                System.out.println("Null action: "+entry);
                return null;
            } 
            // Defaults
            Syntax querySyntax=TestQueryUtils.getQuerySyntax(manifest);
            if (querySyntax!=null && !querySyntax.equals(Syntax.syntaxARQ) && !querySyntax.equals(Syntax.syntaxSPARQL_10) && !querySyntax.equals(Syntax.syntaxSPARQL_11))
                throw new QueryTestException("Unknown syntax: "+querySyntax);
            // May be null
            Resource defaultTestType=TestUtils.getResource(manifest,TestManifestX.defaultTestType);
            // test name,test type,action -> query specific query[+data],results
            Resource testType=defaultTestType;
            if (entry.hasProperty(RDF.type))
                testType=entry.getProperty(RDF.type).getResource();
            TestItem item=null;
            if (testType==null || (!testType.equals(TestManifestUpdate_11.UpdateEvaluationTest) && !testType.equals(TestManifest_11.UpdateEvaluationTest))) 
                item=TestItem.create(entry,defaultTestType,querySyntax,DataFormat.langXML);
            if (testType!=null && (testType.equals(TestManifest.QueryEvaluationTest) || testType.equals(TestManifestX.TestQuery))) {
                Property entRegime=new PropertyImpl(SD, "entailmentRegime");
                Resource owlds=new ResourceImpl(ENT, "OWL-Direct");
                if (action.hasProperty(entRegime) && TestUtils.getResource(action, entRegime).equals(owlds))
                    return new OWLBGPQueryTest(testName,results,fileManager,item);
            }
//        }
        return null;
    }

}
