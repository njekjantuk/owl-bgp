package org.semanticweb.sparql.bgpevaluation.monitor;

import java.io.PrintStream;


public class MinimalPrintingMonitor extends TimingMonitor {
    protected final PrintStream m_out;
    
    public MinimalPrintingMonitor() {
        m_out=System.out;
    }
    public MinimalPrintingMonitor(PrintStream out) {
        m_out=out;
    }
    
    public void bgpEvaluationFinished(int resultSize) {
        super.bgpEvaluationFinished(resultSize);
        m_out.println("BGP Evalation finished in: "+getLastBGPEvaluationTime()+" ms with "+resultSize+" results. ");
    }
}
