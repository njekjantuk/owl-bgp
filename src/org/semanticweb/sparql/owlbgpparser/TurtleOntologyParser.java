package org.semanticweb.sparql.owlbgpparser;

import java.io.BufferedInputStream;
import java.io.IOException;

import org.semanticweb.owlapi.io.OWLOntologyDocumentSource;
import org.semanticweb.owlapi.model.OWLOntology;

public class TurtleOntologyParser extends AbstractOWLParser {
    public void parse(OWLOntologyDocumentSource documentSource, OWLOntology ontology) throws ParseException, IOException {
        TurtleParser parser;
        if (documentSource.isReaderAvailable()) 
            parser=new TurtleParser(documentSource.getReader(), new ConsoleTripleHandler(), documentSource.getDocumentIRI().toString());
        else if(documentSource.isInputStreamAvailable())
            parser=new TurtleParser(documentSource.getInputStream(), new ConsoleTripleHandler(), documentSource.getDocumentIRI().toString());
        else 
            parser=new TurtleParser(new BufferedInputStream(documentSource.getDocumentIRI().toURI().toURL().openStream()), new ConsoleTripleHandler(), documentSource.getDocumentIRI().toString());
        OWLRDFConsumerAdapter consumer=new OWLRDFConsumerAdapter(ontology);
        parser.setTripleHandler(consumer);
        parser.parseDocument();
    }
}
