package org.semanticweb.sparql.owlbgp.wgtests;

import junit.framework.TestSuite;

import org.semanticweb.sparql.bgpevaluation.OWLReasonerStageGenerator;
import org.semanticweb.sparql.bgpevaluation.monitor.MonitorAdapter;

import arq.qtest;
import arq.cmd.TerminationException;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.query.ARQ;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.engine.main.StageBuilder;
import com.hp.hpl.jena.sparql.engine.main.StageGenerator;
import com.hp.hpl.jena.sparql.expr.E_Function;
import com.hp.hpl.jena.sparql.expr.NodeValue;
import com.hp.hpl.jena.sparql.junit.EarlReport;
import com.hp.hpl.jena.sparql.junit.ScriptTestSuiteFactory;
import com.hp.hpl.jena.sparql.junit.SimpleTestRunner;
import com.hp.hpl.jena.sparql.util.NodeFactory;
import com.hp.hpl.jena.sparql.vocabulary.DOAP;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.sparql.vocabulary.TestManifest;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.XSD;

public class TestRunner extends qtest {

    public TestRunner(String[] argv) {
        super(argv);
    }
    
    public static void main (String... argv) {
        StageGenerator orig=(StageGenerator)ARQ.getContext().get(ARQ.stageGenerator);
        StageGenerator hermiTStageGenerator=new OWLReasonerStageGenerator(orig, new MonitorAdapter());
        StageBuilder.setGenerator(ARQ.getContext(), hermiTStageGenerator);
        ARQ.getContext().set(ARQ.optimization, false);
        ARQ.init();
        try {
            new TestRunner(argv).mainRun();
        } catch (TerminationException ex) { 
            System.exit(ex.getCode()); 
        }
    }
    @Override
    protected void exec() {
        if (cmdStrictMode)
            ARQ.setStrictMode() ;
        
        if (suite!=null)
            SimpleTestRunner.runAndReport(suite) ;
        else {
            // running a manifest
            NodeValue.VerboseWarnings=false;
            E_Function.WarnOnUnknownFunction=false;
//            if (createEarlReport)
                oneManifestEarl(testfileAbs) ;
//            else
//                oneManifest(testfileAbs) ;
        }
    }
    static void oneManifestEarl(String testManifest) {
        String name= "SPARQLing HermiT";
        String releaseName= "SPARQLing HermiT";
        String version="0.0"; //ARQ.VERSION;
        String homepage="http://code.google.com/p/owl-bgp/";
        String systemURI = "http://code.google.com/p/owl-bgp/" ;  // Null for bNode.
        
        // Include information later.
        EarlReport report = new EarlReport(systemURI, name, version, homepage) ;
        ScriptTestSuiteFactory.results = report ;
        
        Model model = report.getModel() ;
        model.setNsPrefix("dawg", TestManifest.getURI()) ;
        
//        // Update the EARL report. 
//        Resource jena = model.createResource()
//                    .addProperty(FOAF.homepage, model.createResource("http://jena.apache.org/")) ;
//        

        // Birte runs the report
        Resource who = model.createResource(FOAF.Person)
                .addProperty(FOAF.name, "Birte Glimm")
                .addProperty(FOAF.homepage, 
                             model.createResource("http://www.uni-ulm.de/in/ki/glimm")) ;
        
        Resource reporter = report.getReporter() ;
        reporter.addProperty(DC.creator, who) ;
        
        model.setNsPrefix("doap", DOAP.getURI()) ; 
        model.setNsPrefix("xsd", XSD.getURI()) ;
        
        // DAWG specific stuff.
        Resource system = report.getSystem() ;
        system.addProperty(RDF.type, DOAP.Project) ;
        system.addProperty(DOAP.name, name) ;
        system.addProperty(DOAP.homepage, homepage) ;
        system.addProperty(DOAP.maintainer, who) ;
        
        Resource release = model.createResource(DOAP.Version) ;
        system.addProperty(DOAP.release, release) ;
        
        Node today_node = NodeFactory.todayAsDate() ;
        Literal today = model.createTypedLiteral(today_node.getLiteralLexicalForm(), today_node.getLiteralDatatype()) ;
        release.addProperty(DOAP.created, today) ;
        release.addProperty(DOAP.name, releaseName) ;      // Again
        
        TestSuite suite = new OWLBGPScriptTestSuitFactory().process(testManifest);// generates just the entailment tests with OWL Direct Semantics
        SimpleTestRunner.runSilent(suite) ;
        
        OWLBGPScriptTestSuitFactory.results.getModel().write(System.out, "TTL") ;
    }
}
