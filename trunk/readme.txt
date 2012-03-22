OWL-BGP is a SPARQL implementation based on ARQ 
(http://jena.sourceforge.net/ARQ/) where basic graph patterns are 
evaluated with OWL 2 Direct Semantics. Thus, OWL-BGP implements the 
SPARQL OWL Direct Semantics entailment regime. Internally, any OWL 2 DL 
reasoner that implements the OWLReasoner interface of the OWL API 
(http://owlapi.sourceforge.net/) can be used although the default 
reasoner for OWL-BGP is HermiT (http://hermit-reasoner.com).    
For HermiT a special cost-based query atom ordering is performed, 
otherwise only limited cost-based ordering for query atoms is 
available. 

The main class of the implementation is GUI_Demo in the package 
org.semanticweb.sparql which the user can run using eclipse or 
some other editor. The location where the queried ontology has 
been saved should be given as a program argument and whether the
user wants static or dynamic ordering to be performed on the atoms
of the queries should be given as a VM argument in the form 
-Dordering="Dynamic" (for dynamic ordering) or -Dordering="Static"
(for static ordering). The default is static ordering. 

OWL-BGP is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.
   
OWL-BGP is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.
   
Copies of the GNU General Public License and the GNU Lesser General Public 
License have been included with this distribution in the file `gpl.txt` and 
lgpl-3.0.txt, respectively. An online version is available at
<http://www.gnu.org/licenses/>.

More information about OWL-BGP is available at 
<http://code.google.com/p/owl-bgp/>, or by contacting Birte Glimm
at the Oxford University Computing Laboratory.

OWL-BGP uses the following libraries in unmodified form:

1) ARQ and ARQ-tests 
   http://jena.sourceforge.net/ARQ/
   see lib/ARQ.LICENSE
2) Jena and Jena-tests 
   http://jena.sourceforge.net/
   see lib/Jena.LICENSE 
3) The OWL API, 
   http://owlapi.sourceforge.net
   released under LGPL 3.0, see lib/owlapi.LICENSE
4) Hermit, http://hermit-reasoner.com
   released under LGPL 3.0, see lib/HermiT.LICENSE
5) ICU4J,
   http://site.icu-project.org/ 
   see lib/ICU4J.LICENSE
6) IRI 
   http://jena.sourceforge.net/iri/
   see lib/IRI.LICENSE
7) Log4j,
   http://logging.apache.org/log4j/ 
   released under The Apache Software License, Version 2.0, 
   see lib/Log4j.LICENSE
8) slf4j-api and slf4j-log4j12,
   http://slf4j.org/ 
   released under MIT License, see lib/slf4j.LICENSE
9) Xerces,
   http://xerces.apache.org/xerces2-j/ 
   released under The Apache Software License, Version 2.0, 
   see lib/xerces.LICENSE