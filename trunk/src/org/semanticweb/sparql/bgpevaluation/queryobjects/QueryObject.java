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

package  org.semanticweb.sparql.bgpevaluation.queryobjects;

import java.util.List;
import java.util.Map;

import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.Variable;

public interface QueryObject<T> {
    public T getAxiomTemplate();
	public List<Atomic[]> computeBindings(List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions);
//	public int getCurrentCost(Reasoner reasoner, List<Atomic[]> candidateBindings, Map<Variable,Integer> bindingPositions, OWLOntologyGraph graph);
	<O> O accept(QueryObjectVisitorEx<O> visitor);
}
