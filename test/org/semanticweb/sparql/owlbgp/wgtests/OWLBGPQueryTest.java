package org.semanticweb.sparql.owlbgp.wgtests;

import java.util.List;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.sparql.arq.OWLOntologyDataSet;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.sparql.junit.EarlReport;
import com.hp.hpl.jena.sparql.junit.QueryTest;
import com.hp.hpl.jena.sparql.junit.TestItem;
import com.hp.hpl.jena.util.FileManager;

public class OWLBGPQueryTest extends QueryTest {

    public OWLBGPQueryTest(String testName, EarlReport earl, FileManager fm, TestItem t) {
        super(testName, earl, fm, t);
    }
    protected static Dataset createDataset(List<String> defaultGraphURIs, List<String> namedGraphURIs) {
        try {
            return new OWLOntologyDataSet(defaultGraphURIs, namedGraphURIs);
        } catch (OWLOntologyCreationException e) {
            fail("Could not parse the dataset into OWL ontologies.");
            return null;
        }
    }
}
