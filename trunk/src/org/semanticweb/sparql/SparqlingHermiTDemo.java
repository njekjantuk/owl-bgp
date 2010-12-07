package org.semanticweb.sparql;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.semanticweb.HermiT.debugger.ConsoleTextArea;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.sparql.arq.HermiTDataSet;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

public class SparqlingHermiTDemo {
	public static final Font s_monospacedFont=new Font("Monospaced",Font.PLAIN,12);
	public static final String LB = System.getProperty("line.separator") ;
	public static final String STD_PROLOG="PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+LB
	+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "+LB
	+ "PREFIX owl: <http://www.w3.org/2002/07/owl#> "+LB
	+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "+LB
    + "PREFIX swrl: <http://www.w3.org/2003/11/swrl#> "+LB
    + "PREFIX swrlb: <http://www.w3.org/2003/11/swrlb#> "+LB
    + "PREFIX swrlx: <http://www.w3.org/2003/11/swrlx#> "+LB
    + "PREFIX ruleml: <http://www.w3.org/2003/11/ruleml#> "+LB;
	
	protected final ConsoleTextArea consoleTextArea;
	protected final PrintWriter output;
	protected final BufferedReader input;
	protected final JScrollPane scrollPane;
	protected final JFrame mainFrame;
	protected final Dimension screenSize;
	protected final Dimension preferredSize;
	protected final HermiTSPARQLEngine sparqlEngine;
	protected final HermiTDataSet ds;
	protected boolean inMainLoop;
	protected StringBuffer commandStringBuffer;
	
	public SparqlingHermiTDemo(File defaultOntologyFile) throws OWLOntologyCreationException {
		consoleTextArea=new ConsoleTextArea();
		consoleTextArea.setFont(s_monospacedFont);
	    output=new PrintWriter(consoleTextArea.getWriter());
	    input=new BufferedReader(consoleTextArea.getReader());
        scrollPane=new JScrollPane(consoleTextArea);
        scrollPane.setPreferredSize(new Dimension(800,300));
        mainFrame=new JFrame("Sparqling HermiT's Demo GUI");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setContentPane(scrollPane);
        mainFrame.pack();
        screenSize=Toolkit.getDefaultToolkit().getScreenSize();
        preferredSize=mainFrame.getPreferredSize();
        mainFrame.setLocation((screenSize.width-preferredSize.width)/2,screenSize.height-100-preferredSize.height);
        commandStringBuffer=new StringBuffer();
        mainFrame.setVisible(true);
        output.println("Loading and preparing the default ontlogy...");
        ds=new HermiTDataSet(defaultOntologyFile);
        sparqlEngine=new HermiTSPARQLEngine();
        output.println("The ontology "+ds.getDefaultGraph().getReasoner().getRootOntology()+" is set-up as default graph. ");
        output.println("Please enter a query (possibly on multiple lines) followed by the keyword 'GO'. ");
        output.println("Prefixes for rdf, rdfs, owl, xsd, swrl, swrlb, swrlx, and ruleml will automatically be added. ");
	}
	
	public static void main(String[] args) throws OWLOntologyCreationException {
		File defaultOntologyFile;
		if (args.length>0 && args[0]!=null) {
			defaultOntologyFile=new File(args[0]);
		} else {
			defaultOntologyFile=new File("src/ontologies/pizza.owl");
		}
		SparqlingHermiTDemo demo=new SparqlingHermiTDemo(defaultOntologyFile);
		demo.mainLoop();
	}
	public void mainLoop() {
        try {
            inMainLoop=true;
            while (inMainLoop) {
                output.print("> ");
                String commandLine=input.readLine();
                if (commandLine!=null) {
                    commandLine=commandLine.trim();
                    processCommandLine(commandLine);
                }
            }
            output.flush();
        } catch (IOException e) {}
    }
	public void processCommandLine(String commandLine) {
		if (commandLine.endsWith("GO")) {
			commandStringBuffer.append(commandLine.substring(0, commandLine.length()-2));
			try {
				ResultSet results=sparqlEngine.execQuery(STD_PROLOG+commandStringBuffer.toString(),ds);
		        // new we have a plan, all iterators are arranged in the required order to go over 
				// the results until we have what we were asked for
				// the real computation starts when we ask for the first result (below)
		        while (results.hasNext()) {
		        	QuerySolution rb=results.nextSolution();
					output.println(rb);
		        }
		        output.println();
				commandStringBuffer=new StringBuffer();
			} catch (Exception e) {
				output.println("Error: "+e.getMessage());
			}
		} else {
			commandStringBuffer.append(commandLine);
		}
    }
}
