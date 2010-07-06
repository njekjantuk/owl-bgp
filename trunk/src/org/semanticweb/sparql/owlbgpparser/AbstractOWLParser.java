package org.semanticweb.sparql.owlbgpparser;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLConnection;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipInputStream;

public abstract class AbstractOWLParser {

    protected String getRequestTypes() {
        return "application/rdf+xml, application/xml; q=0.5, text/xml; q=0.3, */*; q=0.2";
    }

    /**
     * A convenience method that obtains an input stream from a URI.
     * This method sets up the correct request type and wraps the input
     * stream within a buffered input stream
     * @param documentIRI The URI from which the input stream should be returned
     * @return The input stream obtained from the URI
     * @throws IOException if there was an <code>IOException</code> in obtaining the input stream from the URI.
     */
    protected InputStream getInputStream(String documentIRI) throws IOException {
            String requestType = getRequestTypes();
            URLConnection conn = URI.create(documentIRI).toURL().openConnection();
            conn.addRequestProperty("Accept", requestType);
            if (IOProperties.getInstance().isConnectionAcceptHTTPCompression())
                conn.setRequestProperty("Accept-Encoding","gzip, deflate");
            conn.setConnectTimeout(IOProperties.getInstance().getConnectionTimeout());
            InputStream is;
            if ("gzip".equals(conn.getContentEncoding())) // test works OK even if CE is null
				is = new BufferedInputStream(new GZIPInputStream(conn.getInputStream()));
			else if ("deflate".equals(conn.getContentEncoding()))
                is = new BufferedInputStream(new InflaterInputStream(conn.getInputStream(), new Inflater(true)));
			else is = new BufferedInputStream(conn.getInputStream());
            if (documentIRI.toString().endsWith(".zip")) {
            	ZipInputStream zis = new ZipInputStream(is);
                zis.getNextEntry();
                is = new BufferedInputStream(zis);
            }
            return is;
    }
}
