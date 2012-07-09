/* Copyright 2011 by the Oxford University Computing Laboratory

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

package  org.semanticweb.sparql.bgpevaluation.monitor;

import java.io.PrintStream;

import org.semanticweb.sparql.bgpevaluation.queryobjects.QueryObject;
import org.semanticweb.sparql.owlbgp.model.axioms.Axiom;


public class PrintingMonitor extends TimingMonitor {
    protected final PrintStream m_out;
    
    public PrintingMonitor() {
        m_out=System.out;
    }
    public PrintingMonitor(PrintStream out) {
        m_out=out;
    }
    
    public void bgpEvaluationFinished(int resultSize) {
        super.bgpEvaluationFinished(resultSize);
        m_out.println("BGP Evalation finished in: "+getLastBGPEvaluationTime()+" ms with "+resultSize+" results. ");
    }
    public void bgpParsingFinished() {
        super.bgpParsingFinished();
        m_out.println("BGP parsing finished in: "+getLastBGPParsingTime()+" ms. ");
    }
    public void connectedComponentsComputationFinished(int numComponents) {
        super.connectedComponentsComputationFinished(numComponents);
        m_out.println(getNumberOfComponents()+" connected components computed in: "+getLastConnectedComponentComputationTime()+" ms. ");
    }
    public void queryObjectEvaluationFinished(int results) {
        super.queryObjectEvaluationFinished(results);
        m_out.println("Query object evaluation finished in: "+getLastQueryObjectEvaluationTime()+" ms with "+getLastQueryObjectResultSize()+" results. ");
    }
    public void componentsEvaluationFinished(int results) {
        super.componentsEvaluationFinished(results);
        m_out.println("Component "+(componentIndex-1)+" evaluated in: "+getLastComponentEvaluationTime()+" ms with "+getLastComponentResultSize()+" results. ");
    }
    public void costEvaluationFinished(QueryObject<? extends Axiom> cheapest) {
        super.costEvaluationFinished(cheapest);
        m_out.println("Cost evaluation finished in "+getLastCostEstimationTime()+" ms.");
    }
    public void parsingFinished(String parsedQuery) {
        super.parsingFinished(parsedQuery);
        m_out.println(parsedQuery);
    }
}
